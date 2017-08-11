package com.gamola.todorest;

import com.gamola.todorest.model.Todo;
import com.gamola.todorest.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoRestApplication.class)
@WebAppConfiguration
public class TodoRestApplicationTests {

	private MockMvc mockMvc;

	private HttpMessageConverter jsonConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private TodoRepository todoRepository;

	@Before
	public void setUp(){
		mockMvc = webAppContextSetup(webApplicationContext).build();

		todoRepository.save(new Todo(1L, "first"));
		todoRepository.save(new Todo(2L, "second"));
	}

	@Autowired
	public void setConverters(HttpMessageConverter[] converters){
		jsonConverter = Arrays.asList(converters).stream().filter(c -> c instanceof MappingJackson2HttpMessageConverter).findAny().get();
	}

	@Test
	public void contextLoads() {
		System.out.println("shiii");
	}

	@Test
	public void readTodoSuccess() throws Exception {
		mockMvc.perform(get("/todos/{id}", 1)).
				andExpect(status().is2xxSuccessful()).
				andExpect(jsonPath("$.id", is(1))).
				andExpect(jsonPath("$.text", is("first"))).
				andExpect(jsonPath("$.complete", is(false)));
	}

	@Test
	public void readTodoNotFound() throws Exception {
		mockMvc.perform(get("/todos/{id}", 3)).andExpect(status().is4xxClientError()).andExpect(status().is4xxClientError());
	}

	@Test
	public void createTodo() throws Exception {
		String todoJson = json(new Todo("third"));
		mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(todoJson)).andExpect(status().is2xxSuccessful());
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		jsonConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}

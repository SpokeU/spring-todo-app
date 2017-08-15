package com.gamola.todorest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamola.todorest.model.Todo;
import com.gamola.todorest.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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

    @Autowired
    protected ObjectMapper objectMapper;

    private List<Todo> savedTodos = new ArrayList<>();

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        savedTodos.add(todoRepository.save(new Todo(1L, "first")));
        savedTodos.add(todoRepository.save(new Todo(2L, "second")));
    }

    @Test
    public void readAll() throws Exception {
        mockMvc.perform(get("/todos")).
                andExpect(jsonPath("$[0].id", is(savedTodos.get(0).getId().intValue()))).
                andExpect(jsonPath("$[0].text", is(savedTodos.get(0).getText()))).
                andExpect(jsonPath("$[0].complete", is(savedTodos.get(0).getComplete()))).

                andExpect(jsonPath("$[1].id", is(savedTodos.get(1).getId().intValue()))).
                andExpect(jsonPath("$[1].text", is(savedTodos.get(1).getText()))).
                andExpect(jsonPath("$[1].complete", is(savedTodos.get(1).getComplete())));
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
        String todoJson = objectMapper.writeValueAsString(new Todo("third"));
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(todoJson)).andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/todos/{id}", 3)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteTodo() throws Exception {
        mockMvc.perform(get("/todos/{id}", 1)).andExpect(status().is2xxSuccessful());
        mockMvc.perform(delete("/todos/{id}", 1)).andExpect(status().isAccepted());
        mockMvc.perform(get("/todos/{id}", 1)).andExpect(status().is4xxClientError());
    }

    @Test
    public void updateTodo() throws Exception {
        Todo firstTodo = todoRepository.findOne(1L);
        firstTodo.setComplete(true);
        firstTodo.setText("first updated");

        String updatedTodo = objectMapper.writeValueAsString(firstTodo);
        mockMvc.perform(put("/todos/{id}", 1).contentType(MediaType.APPLICATION_JSON).content(updatedTodo)).andExpect(status().isOk());

        mockMvc.perform(get("/todos/{id}", 1)).
                andExpect(jsonPath("$.id", is(1))).
                andExpect(jsonPath("$.text", is("first updated"))).
                andExpect(jsonPath("$.complete", is(true)));
    }

}

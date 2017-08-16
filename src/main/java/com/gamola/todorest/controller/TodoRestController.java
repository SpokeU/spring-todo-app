package com.gamola.todorest.controller;

import com.gamola.todorest.exception.TodoNotFoundException;
import com.gamola.todorest.model.Todo;
import com.gamola.todorest.repository.TodoRepository;
import com.gamola.todorest.resourcesupport.TodoResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Filter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
public class TodoRestController {

    @Autowired
    private TodoRepository todoRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Resources<TodoResource> all() {
        List<TodoResource> allTodos = todoRepository.findAll().stream().map(TodoResource::new).collect(Collectors.toList());
        return new Resources<>(allTodos);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public TodoResource get(@PathVariable Long id) {
        Todo todo = todoRepository.findOne(id);
        if (todo != null) {
            return new TodoResource(todo);
        } else {
            throw new TodoNotFoundException("Todo not found");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public TodoResource create(@RequestBody Todo todo) {
        return new TodoResource(todoRepository.save(todo));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public TodoResource update(@RequestBody Todo todo) {
        return new TodoResource(todoRepository.save(todo));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        todoRepository.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "err")
    public ResponseEntity error(){
        new Todo().getText().toCharArray();
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}

package com.gamola.todorest.controller;

import com.gamola.todorest.exception.TodoNotFoundException;
import com.gamola.todorest.model.Todo;
import com.gamola.todorest.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoRestController {

    @Autowired
    private TodoRepository todoRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Todo> all(){
        return todoRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public Todo get(@PathVariable Long id){
        return Optional.ofNullable(todoRepository.findOne(id)).orElseThrow(() -> new TodoNotFoundException());
    }

    @RequestMapping(method = RequestMethod.POST)
    public Todo create(@RequestBody Todo todo){
        return todoRepository.save(todo);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Todo update(@RequestBody Todo todo){
        return todoRepository.save(todo);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable Long id){
        todoRepository.delete(id);
    }

}

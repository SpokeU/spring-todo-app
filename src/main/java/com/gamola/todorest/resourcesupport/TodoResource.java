package com.gamola.todorest.resourcesupport;

import com.gamola.todorest.controller.TodoRestController;
import com.gamola.todorest.model.Todo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class TodoResource extends ResourceSupport {

    private Todo todo;

    public TodoResource(Todo todo) {
        this.todo = todo;
        add(ControllerLinkBuilder.linkTo(TodoRestController.class).withRel("all"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TodoRestController.class).get(todo.getId())).withSelfRel());
    }

    public Todo getTodo() {
        return todo;
    }
}

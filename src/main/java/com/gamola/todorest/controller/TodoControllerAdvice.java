package com.gamola.todorest.controller;

import com.gamola.todorest.exception.TodoNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class TodoControllerAdvice {

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseBody
    VndErrors handleTodoNotFound(TodoNotFoundException e){
        return new VndErrors("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    VndErrors handleError(Exception e){
        return  new VndErrors("error", e.getMessage() != null ? e.getMessage() : "Unexpected error");
    }

}

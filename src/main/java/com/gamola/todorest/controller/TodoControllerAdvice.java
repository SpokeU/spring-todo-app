package com.gamola.todorest.controller;

import com.gamola.todorest.exception.TodoNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@ControllerAdvice
public class TodoControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    ResponseEntity handleError(Exception e){
        ResponseStatus responseStatus = e.getClass().getDeclaredAnnotation(ResponseStatus.class);
        VndErrors error = new VndErrors("error", e.getMessage() != null ? e.getMessage() : "Unexpected error");
        return  new ResponseEntity(error, responseStatus != null ? responseStatus.value() : HttpStatus.OK);
    }

}

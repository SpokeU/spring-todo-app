package com.gamola.todorest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Todo {

    @Id
    @GeneratedValue
    private Long id;

    private String text;
    private Boolean isComplete;

    public Todo() {
    }

    public Todo(String text) {
        this(null, text, false);
    }

    public Todo(Long id, String text) {
        this(id ,text, false);
    }

    public Todo(Long id, String text, Boolean isComplete) {
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}

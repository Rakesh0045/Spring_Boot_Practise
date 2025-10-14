package com.jt.todo_app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor //for non-parameterized constructor
public class Todo {
    private int id;
    private String task;
    private boolean completed;


}
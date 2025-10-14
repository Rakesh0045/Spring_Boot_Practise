package com.todo.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.todo.model.Todo;

@Service
public class TodoService {
    private final List<Todo> todos;
    private static int idCounter;

    public TodoService(List<Todo> todos) {
        this.todos = todos;
        // todos = new ArrayList<>();
    }

     public void addTodo(@RequestParam String task) {
        if (!task.isEmpty() && !task.isBlank()) {
            Todo todo = new Todo();
            todo.setId(++idCounter);
            todo.setTask(task);
            
            for (Todo existingTodo : todos) {
                if (existingTodo.getTask().equals(task)) {
                    return;
                }
            }
            todos.add(todo);
        }
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void toggleTodo(int id) {
        Todo todo = getTodoById(id);
        if (todo != null) {
            todo.setCompleted(!todo.isCompleted());
        }
    }

    public void deleteTodo(int id) {
        Todo todo = getTodoById(id);
        if (todo != null) {
            todos.remove(todo);
        }
    }

    public Todo getTodoById(int id) {
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }
}
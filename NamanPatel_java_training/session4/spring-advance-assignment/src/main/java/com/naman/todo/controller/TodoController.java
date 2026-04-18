package com.naman.todo.controller;

import com.naman.todo.dto.TodoRequestDTO;
import com.naman.todo.dto.TodoResponseDTO;
import com.naman.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// this class Handles all API requests related to Todo
@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    // Constructor Injection
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // API to create Todo
    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO dto) {       //it takes json data from client and coverts it into java object
        return ResponseEntity.ok(todoService.createTodo(dto));                         //return status 200
    }

    // API to get all todos
    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    // API to get todo by id
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    // API to update todo
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequestDTO dto) {
        return ResponseEntity.ok(todoService.updateTodo(id, dto));
    }

    // API to delete todo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Todo deleted successfully");
    }
}
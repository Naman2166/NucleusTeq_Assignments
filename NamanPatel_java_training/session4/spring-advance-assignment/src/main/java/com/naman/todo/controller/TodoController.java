package com.naman.todo.controller;

import com.naman.todo.dto.TodoRequestDTO;
import com.naman.todo.dto.TodoResponseDTO;
import com.naman.todo.service.TodoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    //Logger for TodoController class
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);



    // API to create Todo
    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(@Valid @RequestBody TodoRequestDTO dto) {     //it takes json data from client and coverts it into java object
        logger.info("API called: Create Todo with title: {}", dto.getTitle());                      //log message when API is called
        return ResponseEntity.ok(todoService.createTodo(dto));                       //return status 200 with data
    }



    // API to get all todos
    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> getAllTodos() {
        logger.info("API called: Get All Todos");                         //log message when API is called
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    // API to get todo by id
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> getTodoById(@PathVariable Long id) {
        logger.info("API called: Get Todo by id: {}", id);
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    // API to update todo
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequestDTO dto) {
        logger.info("API called: Update Todo with id: {}", id);
        return ResponseEntity.ok(todoService.updateTodo(id, dto));
    }

    // API to delete todo
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        logger.info("API called: Delete Todo with id: {}", id);
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Todo deleted successfully");
    }
}
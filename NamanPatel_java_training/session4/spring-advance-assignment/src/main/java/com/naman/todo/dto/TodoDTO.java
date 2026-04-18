package com.naman.todo.dto;

import com.naman.todo.enums.TodoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


// this class represents DTO for Todo with validation rules
public class TodoDTO {

    private Long id;                   //sent by backend in response, not by user in input

    @NotNull(message = "Title is required")
    @Size(min = 3, message = "Title must have at least 3 characters")
    private String title;              //title cannot be null and should have atleast 3 characters

    private String description;

    private TodoStatus status;

    private LocalDateTime createdAt;     // sent by backend, not by user




    // Default Constructor
    public TodoDTO() {
    }

    // Parameterized Constructor
    public TodoDTO(Long id, String title, String description, TodoStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }


    // Getters and Setters for each fields

    //id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    //title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    //description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    //status
    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }


    //createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

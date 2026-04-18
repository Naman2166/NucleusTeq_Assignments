package com.naman.todo.dto;

import com.naman.todo.enums.TodoStatus;
import java.time.LocalDateTime;


// DTO for sending response
public class TodoResponseDTO {

    private Long id;                   //sent by backend in response, not by user in input
    private String title;
    private String description;
    private TodoStatus status;
    private LocalDateTime createdAt;     // sent by backend, not by user




    // Default Constructor
    public TodoResponseDTO() {
    }

    // Parameterized Constructor
    public TodoResponseDTO(Long id, String title, String description, TodoStatus status, LocalDateTime createdAt) {
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

package com.naman.todo.dto;

import com.naman.todo.enums.TodoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


// this class represents DTO for Todo with validation rules
public class TodoDTO {

    @NotNull(message = "Title is required")
    @Size(min = 3, message = "Title must have at least 3 characters")
    private String title;              //title cannot be null and should have atleast 3 characters

    private String description;

    private TodoStatus status;



    // Default Constructor
    public TodoDTO() {
    }

    // Parameterized Constructor
    public TodoDTO(String title, String description, TodoStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }


    // Getters and Setters for each fields

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
}

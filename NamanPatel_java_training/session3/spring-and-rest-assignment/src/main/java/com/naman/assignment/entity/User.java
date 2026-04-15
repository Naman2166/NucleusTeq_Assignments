package com.naman.assignment.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// This class represents a User object
public class User {

    private Integer id;                          // it stores unique id automatically for each user

    @NotBlank(message = "Name is required")      // it cannot be null or empty
    private String name;

    @NotNull(message = "Age is required")        // age cannot be null
    private Integer age;                         // used Integer wrapper class here to store null values (when age field is missing in request)

    @NotBlank(message = "Role is required ADMIN/USER")
    private String role;



    // Default constructor
    public User() {}

    // Parametrized Constructor with all fields
    public User(Integer id, String name, Integer age, String role) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.role = role;
    }


    // Getter and Setter methods for each field

    //id
    public Integer getId() {
        return id;                   // return user id
    }

    public void setId(Integer id) {
        this.id = id;                 // set user id
    }


    //name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //age
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    //role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
package com.naman.assignment.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


// This class represents the Employee data
public class Employee {

    private int id;               //id will be auto generated in service class

    @NotBlank(message = "First name is required")      //required field
    private String firstName;

    private String lastName;

    @NotBlank(message = "Department is required")
    private String department;                         // it stores employee department like IT, Sales, HR

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")           // Email should be valid
    private String email;



    //Default constructor
    public Employee() {
    }

    // Parameterized constructor (it is used while creating object)
    public Employee(int id, String firstName, String lastName, String email, String department) {
        this.id = id;                     //here 'this' keyword represents current object of class
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }



    // Getters and Setters method (it is used here for getting and updating private fields value)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    // Getters and Setters method for firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    // Getters and Setters method for lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    // Getters and Setters method for department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    // Getters and Setters method for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
package com.naman.assignment.controller;

import com.naman.assignment.entity.Employee;
import com.naman.assignment.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// This class handles all REST API requests related to Employee
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // constructor injection
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    // API to get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


    // API to get employee by id
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {        //this annotation is used to get id from requested URL
        return employeeService.getEmployeeById(id);
    }


    // API to create new employee
    @PostMapping
    public Employee addEmployee(@Valid @RequestBody Employee emp) {        //this annotation is used to convert JSON request to Employee object
        return employeeService.addEmployee(emp);
    }


    // API to delete employee
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable int id) {
        return employeeService.deleteEmployee(id);
    }


    // API to update employee
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable int id, @Valid @RequestBody Employee emp) {
        return employeeService.updateEmployee(id, emp);
    }
}
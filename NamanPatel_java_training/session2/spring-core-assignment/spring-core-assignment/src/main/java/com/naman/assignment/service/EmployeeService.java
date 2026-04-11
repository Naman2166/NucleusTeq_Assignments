package com.naman.assignment.service;

import com.naman.assignment.entity.Employee;
import com.naman.assignment.exception.EmployeeNotFoundException;
import com.naman.assignment.exception.InvalidRequestException;
import com.naman.assignment.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;


// This class contains all business logic for Employee
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Counter to automatically generate employee id
    // it starts from 4 because we already have 3 dummy employees data
    private int counter = 4;

    //constructor
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }



    // method to get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();            //calling findAll() method from EmployeeRepository class
    }



    // method to get employee by ID
    public Employee getEmployeeById(int id) {

        Employee emp = employeeRepository.findById(id);

        // if employee not found then throw custom exception
        if (emp == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }

        return emp;
    }



    // method to add new employee
    public Employee addEmployee(Employee emp) {

        // checking if user is sending id (it is not allowed bcoz we will generate it here)
        if (emp.getId() != null) {
            throw new InvalidRequestException(
                    "Do not provide employee ID. It will be automatically generated in the backend"
            );
        }

        // increments counter to generate new id (it means id generates automatically for each new employee)
        emp.setId(counter++);

        return employeeRepository.save(emp);
    }



    // method to delete employee
    public String deleteEmployee(int id) {

        boolean isDeleted = employeeRepository.deleteById(id);

        //throw custom exception if employee not found
        if (!isDeleted) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }

        return "Employee deleted successfully";
    }



    // method to update employee
    public Employee updateEmployee(int id, Employee emp) {

        Employee updatedEmp = employeeRepository.update(id, emp);

        //throw exception if employee not found
        if (updatedEmp == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }

        return updatedEmp;
    }
}
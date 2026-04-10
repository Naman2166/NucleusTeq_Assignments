package com.naman.assignment.repository;

import com.naman.assignment.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


// This class work like dummy database using a list
@Repository
public class EmployeeRepository {

    // list to store employee data
    private final List<Employee> employeeList = new ArrayList<>();


    // added some dummy data
    public EmployeeRepository() {
        employeeList.add(new Employee(1, "Naman", "Patel", "naman@gmail.com", "IT"));
        employeeList.add(new Employee(2, "Virat", "Kohli", "virat@gmail.com", "HR"));
        employeeList.add(new Employee(3, "Rohit", "Sharma", "rohit@gmail.com", "Sales"));
    }



    // method to get all employees
    public List<Employee> findAll() {
        return employeeList;
    }


    // method to save new employee
    public Employee save(Employee emp) {
        employeeList.add(emp);
        return emp;
    }


    // method to find employee by id
    public Employee findById(int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                return emp;              // return employee if found
            }
        }

        return null;      // return null if not found
    }


    // method to delete employee by ID
    public boolean deleteById(int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                employeeList.remove(emp);     // remove employee
                return true;
            }
        }

        return false;     // return false if not found
    }


    // method to update employee by id
    public Employee update(int id, Employee updatedEmp) {

        for (Employee emp : employeeList) {
            if (emp.getId() == id) {

                // updating data in same object
                emp.setFirstName(updatedEmp.getFirstName());
                emp.setLastName(updatedEmp.getLastName());
                emp.setEmail(updatedEmp.getEmail());
                emp.setDepartment(updatedEmp.getDepartment());

                return emp;       // return employee with updated data
            }
        }

        return null;     // return null if employee not found
    }
}
package com.naman.assignment.controller;

import com.naman.assignment.entity.User;
import com.naman.assignment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// this class handles all API requests related to users
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // API to search users
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String name,       //these parameters are optional and can be skipped or partially provided in the request to get desired result
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String role
    ) {

        //calls service layer to get users
        List<User> users = userService.searchUsers(name, age, role);

        return ResponseEntity.ok(users);       //returns result with status 200
    }


    // API to submit user
    @PostMapping("/submit")
    public ResponseEntity<String> submitUser(@RequestBody User user) {

        String response = userService.submitUser(user);

        return ResponseEntity.status(201).body(response);     // returns response with status 201 created
    }


    // API to delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id, @RequestParam(required = false) Boolean confirm) {

        String response = userService.deleteUser(id, confirm);

        return ResponseEntity.ok(response);    // returns response with status 200
    }
}
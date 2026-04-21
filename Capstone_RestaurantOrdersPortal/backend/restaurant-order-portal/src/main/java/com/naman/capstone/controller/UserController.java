package com.naman.capstone.controller;

import com.naman.capstone.dto.UserRequestDTO;
import com.naman.capstone.dto.UserResponseDTO;
import com.naman.capstone.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//this class handles all API request related to user
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // API to register a new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO>  registerUser(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(dto));
    }
}

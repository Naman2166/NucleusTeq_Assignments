package com.naman.capstone.controller;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.LoginRequestDTO;
import com.naman.capstone.dto.request.RegisterRequestDTO;
import com.naman.capstone.dto.response.LoginResponseDTO;
import com.naman.capstone.dto.response.UserResponseDTO;
import com.naman.capstone.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class handles all API requests related to users
 */
@RestController
@RequestMapping(AppConstants.USER_BASE_URL)
public class UserController {

    private final UserService userService;

    public UserController(UserService authService) {
        this.userService = authService;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    /**
     * API to register a new user
     * @param registerRequestDTO registration data from client
     * @return registered user details
     */
    @PostMapping(AppConstants.REGISTER)
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        log.info("User registration request received");
        UserResponseDTO response = userService.register(registerRequestDTO);
        log.info("User registered successfully with email={}", registerRequestDTO.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * API for login
     * @param loginRequestDTO login credentials from client
     * @return login response containing token and user details
     */
    @PostMapping(AppConstants.LOGIN)
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Login request received for email={}", loginRequestDTO.getEmail());
        LoginResponseDTO response = userService.login(loginRequestDTO);
        log.info("User logged in successfully with email={}", loginRequestDTO.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}


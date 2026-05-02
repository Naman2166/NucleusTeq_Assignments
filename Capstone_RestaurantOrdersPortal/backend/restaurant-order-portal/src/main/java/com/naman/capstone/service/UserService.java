package com.naman.capstone.service;

import com.naman.capstone.dto.request.LoginRequestDTO;
import com.naman.capstone.dto.request.RegisterRequestDTO;
import com.naman.capstone.dto.response.LoginResponseDTO;
import com.naman.capstone.dto.response.UserResponseDTO;

/**
 * defines methods related to user operations
 */
public interface UserService {

    /**
     * creates a new user
     * @param registerRequestDTO registration data from client
     * @return saved user details
     */
    UserResponseDTO register(RegisterRequestDTO registerRequestDTO);


    /**
     * authenticate user and generate Jwt token
     * @param loginRequestDTO login data from client
     * @return login response containing token and user details
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}

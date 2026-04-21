package com.naman.capstone.service;

import com.naman.capstone.dto.UserRequestDTO;
import com.naman.capstone.dto.UserResponseDTO;

//Service layer for user operations
public interface UserService {

    //user registration
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

}
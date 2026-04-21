package com.naman.capstone.service.impl;

import com.naman.capstone.dto.UserRequestDTO;
import com.naman.capstone.dto.UserResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.UserAlreadyExistsException;
import com.naman.capstone.mapper.UserMapper;
import com.naman.capstone.repository.UserRepository;
import com.naman.capstone.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


//this class contains implementation for user operations
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    //constructor injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserResponseDTO registerUser(UserRequestDTO dto) {

        //checking if email already exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = UserMapper.toEntity(dto);
        user.setWalletBalance(new BigDecimal("1000"));    //setting default wallet balance

        User savedUser =  userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }



}

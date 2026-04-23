package com.naman.capstone.service.impl;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.LoginRequestDTO;
import com.naman.capstone.dto.request.RegisterRequestDTO;
import com.naman.capstone.dto.response.LoginResponseDTO;
import com.naman.capstone.dto.response.UserResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.UserAlreadyExistsException;
import com.naman.capstone.exception.UserNotFoundException;
import com.naman.capstone.mapper.UserMapper;
import com.naman.capstone.repository.UserRepository;
import com.naman.capstone.security.JwtUtil;
import com.naman.capstone.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * this class contains logic for all user operations
 */
@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserResponseDTO register(RegisterRequestDTO requestDto) {

        //checking if email already exists
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = UserMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));     //here we have encrypted password before saving
        user.setWalletBalance(AppConstants.DEFAULT_WALLET_BALANCE);       //setting default wallet balance

        User savedUser =  userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }


    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDto) {

        //authenticate user credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword())
        );

        //fetching user from database
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));

        //generate Jwt token
        String token = jwtUtil.generateToken(user);

        LoginResponseDTO responseDto = new LoginResponseDTO();
        responseDto.setToken(token);
        responseDto.setUser(UserMapper.toResponseDTO(user));
        responseDto.setRole(user.getRole());

        return responseDto;
    }
}


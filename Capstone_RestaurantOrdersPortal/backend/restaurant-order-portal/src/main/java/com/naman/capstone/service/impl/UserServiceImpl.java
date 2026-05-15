package com.naman.capstone.service.impl;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.LoginRequestDTO;
import com.naman.capstone.dto.request.RegisterRequestDTO;
import com.naman.capstone.dto.response.LoginResponseDTO;
import com.naman.capstone.dto.response.UserResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.exception.UserAlreadyExistsException;
import com.naman.capstone.mapper.UserMapper;
import com.naman.capstone.repository.UserRepository;
import com.naman.capstone.security.JwtUtil;
import com.naman.capstone.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Base64;

/**
 * this class contains logic for all user operations
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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


    /**
     * registers a new user
     * @param requestDto registration details 
     * @return registered user
     */
    @Override
    public UserResponseDTO register(RegisterRequestDTO requestDto) {
        logger.info("Request received to register new user with email: {}", requestDto.getEmail());

        //checking if email already exists
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            logger.warn("registration failed, user already exists with email: {}", requestDto.getEmail());
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        //decode base64 password sent from frontend to original form
        String decodedPassword = new String(Base64.getDecoder().decode(requestDto.getPassword()));

        User user = UserMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(decodedPassword));        //here i have encrypted password before saving
        user.setWalletBalance(AppConstants.DEFAULT_WALLET_BALANCE);       //setting default wallet balance

        User savedUser =  userRepository.save(user);
        logger.info("successfully registered user with email: {}", savedUser.getEmail());

        return UserMapper.toResponseDTO(savedUser);
    }


    /**
     * authenticates a user and generates a jwt token
     * @param requestDto login credentials
     * @return response containing jwt token and user details
     */
    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDto) {
        logger.info("login request received for user with email: {}", requestDto.getEmail());

        //decode base64 password sent from frontend to original form
        String decodedPassword = new String(Base64.getDecoder().decode(requestDto.getPassword()));

        //authenticate user credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        decodedPassword
                )
        );

        //fetching user from database
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with this email not found"));

        //generate Jwt token
        String token = jwtUtil.generateToken(user);
        logger.info("successfully authenticated user and generated token for email: {}", user.getEmail());

        LoginResponseDTO responseDto = new LoginResponseDTO();
        responseDto.setToken(token);
        responseDto.setUser(UserMapper.toResponseDTO(user));
        responseDto.setRole(user.getRole());

        return responseDto;
    }
}
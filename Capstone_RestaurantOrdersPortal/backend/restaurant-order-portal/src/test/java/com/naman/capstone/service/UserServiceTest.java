package com.naman.capstone.service;

import com.naman.capstone.dto.request.LoginRequestDTO;
import com.naman.capstone.dto.request.RegisterRequestDTO;
import com.naman.capstone.dto.response.LoginResponseDTO;
import com.naman.capstone.dto.response.UserResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.repository.UserRepository;
import com.naman.capstone.security.JwtUtil;
import com.naman.capstone.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * tests user service login functionality
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * tests login success with valid credentials
     */
    @Test
    void login_success() {

        // arrange
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(user))
                .thenReturn("mocked-jwt-token");

        // act
        LoginResponseDTO response = userService.login(request);

        // assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals("test@gmail.com", response.getUser().getEmail());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(jwtUtil, times(1)).generateToken(user);
    }



    /**
     * tests login failure when user not found
     */
    @Test
    void login_user_not_found() {

        // arrange
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("notfound@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> userService.login(request));

        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail(request.getEmail());
    }


    /**
     * tests register success with valid data
     */
    @Test
    void register_success() {

        // arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // act
        UserResponseDTO response = userService.register(request);

        // assert
        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }


    /**
     * tests register failure when email already exists
     */
    @Test
    void register_user_already_exists() {

        // arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@gmail.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // act & assert
        assertThrows(RuntimeException.class, () -> userService.register(request));

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any());
    }
}
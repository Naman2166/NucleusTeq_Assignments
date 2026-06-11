package com.naman.capstone.service;

import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.repository.UserRepository;
import com.naman.capstone.service.impl.CurrentUserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * tests current user service functionality
 */
@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CurrentUserServiceImpl currentUserService;


    /**
     * testing get current user success
     */
    @Test
    void get_current_user_success() {

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("naman@gmail.com");

        User user = new User();
        user.setEmail("naman@gmail.com");

        when(userRepository.findByEmail("naman@gmail.com")).thenReturn(Optional.of(user));
        User result = currentUserService.getCurrentUser(userDetails);

        assertNotNull(result);
        assertEquals("naman@gmail.com", result.getEmail());
    }


    /**
     * testing get current user when user not found
     */
    @Test
    void get_current_user_not_found() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("naman@gmail.com");
        when(userRepository.findByEmail("naman@gmail.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                currentUserService.getCurrentUser(userDetails));
    }


}
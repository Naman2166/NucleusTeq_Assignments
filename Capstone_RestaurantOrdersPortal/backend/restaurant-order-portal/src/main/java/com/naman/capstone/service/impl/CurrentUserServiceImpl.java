package com.naman.capstone.service.impl;

import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.repository.UserRepository;
import com.naman.capstone.service.CurrentUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * this class retrieves full user entity from database using authenticated user details
 */
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * gets the user entity using UserDetails
     * @param userDetails current authenticated user details
     * @return User entity
     */
    @Override
    public User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
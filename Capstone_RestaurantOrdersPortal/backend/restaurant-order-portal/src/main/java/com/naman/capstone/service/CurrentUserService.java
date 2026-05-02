package com.naman.capstone.service;

import com.naman.capstone.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * this interface is responsible for retrieving user entity from authenticated UserDetails
 */
public interface CurrentUserService {

    /**
     * gets the user entity using UserDetails
     * @param userDetails authenticated user details
     * @return User entity
     */
    User getCurrentUser(UserDetails userDetails);
}
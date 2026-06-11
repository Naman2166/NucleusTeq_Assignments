package com.naman.capstone.security;

import com.naman.capstone.entity.User;
import com.naman.capstone.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * This class fetch user data from database for authentication
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //constructor injection
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * fetch user details by email for authentication
     * @param email user email
     * @return UserDetails object for spring security
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //fetch user from database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email"));

        //convert user entity to spring security User
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()))
        );
    }
}
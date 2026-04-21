package com.naman.capstone.repository;

import com.naman.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

//Repository for User entity to perform database operations
public interface UserRepository extends JpaRepository<User, Long> {

    //finds user by email
    Optional<User> findByEmail(String email);

    //checks if email already exists
    boolean existsByEmail(String email);
}
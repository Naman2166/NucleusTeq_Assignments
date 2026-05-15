package com.naman.capstone.repository;

import com.naman.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Provides database operations for user entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * find user by email
     * @param email user email
     * @return user
     */
    Optional<User> findByEmail(String email);

    /**
     * check email already exists or not
     * @param email user email
     * @return true if exists else false
     */
    boolean existsByEmail(String email);
}
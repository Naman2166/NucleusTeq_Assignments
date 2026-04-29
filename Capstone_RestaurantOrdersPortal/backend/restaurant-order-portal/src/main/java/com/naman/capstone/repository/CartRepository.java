package com.naman.capstone.repository;

import com.naman.capstone.entity.Cart;
import com.naman.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for Cart entity operations
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds cart by user
     * @param user owner of the cart
     * @return cart details
     */
    Optional<Cart> findByUser(User user);
}
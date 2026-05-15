package com.naman.capstone.repository;

import com.naman.capstone.entity.Cart;
import com.naman.capstone.entity.CartItem;
import com.naman.capstone.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CartItem entity operations
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Finds a cart item by cart and menu item
     * @param cart cart
     * @param menuItem menu item
     * @return cart item
     */
    Optional<CartItem> findByCartAndMenuItem(Cart cart, MenuItem menuItem);


    /**
     * Finds a cart item by id and cart
     * @param id cart item id
     * @param cart the cart to which the item should belong
     * @return cart item
     */
    Optional<CartItem> findByIdAndCart(Long id, Cart cart);


}
package com.naman.capstone.service;

import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.User;

/**
 * Defines cart related operations
 */
public interface CartService {

    /**
     * Add new item to user cart
     * @param user logged-in user
     * @param menuItemId menu item id
     * @param quantity quantity to add
     * @return cart data
     */
    CartResponseDTO addItem(User user, Long menuItemId, int quantity);

    /**
     * Gets user cart with all items
     * @param user logged-in user
     * @return cart details
     */
    CartResponseDTO getCart(User user);

    /**
     * Removes an item from cart
     * @param user logged-in user
     * @param cartItemId cart item id
     */
    void removeItem(User user, Long cartItemId);

    /**
     * Clears entire cart
     * @param user logged-in user
     */
    void clearCart(User user);
}
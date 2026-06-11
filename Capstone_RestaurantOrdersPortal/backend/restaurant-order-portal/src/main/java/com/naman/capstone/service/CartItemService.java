package com.naman.capstone.service;

import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.User;

/**
 * Defines cart item related operations
 */
public interface CartItemService {

    /**
     * Update quantity of a cart item
     * @param user logged-in user
     * @param cartItemId cart item id
     * @param quantity new quantity
     * @return updated cart
     */
    CartResponseDTO updateQuantity(User user, Long cartItemId, int quantity);

}
package com.naman.capstone.mapper;

import com.naman.capstone.dto.response.CartItemResponseDTO;
import com.naman.capstone.entity.CartItem;

/**
 * Maps cartItem entity to DTO
 */
public class CartItemMapper {

    //mapping entity to dto
    public static CartItemResponseDTO toDTO(CartItem item) {
        return new CartItemResponseDTO(
                item.getId(),
                item.getMenuItem().getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getMenuItem().getPrice()
        );
    }
}
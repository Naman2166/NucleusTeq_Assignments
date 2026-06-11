package com.naman.capstone.mapper;

import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.dto.response.CartItemResponseDTO;
import com.naman.capstone.entity.Cart;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps Cart entity to DTO
 */
public class CartMapper {

    public static CartResponseDTO toDTO(Cart cart) {

        List<CartItemResponseDTO> items = cart.getItems()
                .stream()
                .map(item -> CartItemMapper.toDTO(item))
                .collect(Collectors.toList());

        return new CartResponseDTO(
                cart.getId(),
                cart.getTotalPrice(),
                items
        );
    }
}
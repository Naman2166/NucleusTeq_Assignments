package com.naman.capstone.mapper;

import com.naman.capstone.dto.response.MenuItemResponseDTO;
import com.naman.capstone.entity.MenuItem;

/**
 * Mapper class for converting category entity and DTO
 */
public class MenuItemMapper {

    public static MenuItemResponseDTO toDTO(MenuItem item) {

        MenuItemResponseDTO dto = new MenuItemResponseDTO();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setCategoryId(item.getCategory().getId());
        dto.setRestaurantId(item.getRestaurant().getId());

        return dto;
    }
}

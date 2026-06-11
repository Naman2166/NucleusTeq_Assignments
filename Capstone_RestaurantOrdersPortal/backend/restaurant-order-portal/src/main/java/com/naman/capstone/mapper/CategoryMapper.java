package com.naman.capstone.mapper;

import com.naman.capstone.dto.response.CategoryResponseDTO;
import com.naman.capstone.entity.Category;

/**
 * Mapper class for converting category entity and DTO
 */
public class CategoryMapper {

    //mapping entity to dto
    public static CategoryResponseDTO toDTO(Category category) {

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setRestaurantId(category.getRestaurant().getId());

        return dto;
    }
}

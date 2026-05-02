package com.naman.capstone.mapper;

import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.Restaurant;

/**
 * Mapper class for converting Restaurant entity and DTO.
 */
public class RestaurantMapper {

    //mapping entity to dto
    public static RestaurantResponseDTO toDTO(Restaurant restaurant) {

        RestaurantResponseDTO dto = new RestaurantResponseDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setAddress(restaurant.getAddress());
        dto.setStatus(restaurant.getStatus());

        return dto;
    }

}
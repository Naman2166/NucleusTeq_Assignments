package com.naman.capstone.service;

import com.naman.capstone.dto.request.RestaurantRequestDTO;
import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.User;

import java.util.List;

/**
 * defines methods for restaurant operations
 */
public interface RestaurantService {

    /**
     * Create a new restaurant
     * @param requestDTO details for creating a restaurant
     * @param owner owner creating a restaurant
     * @return created restaurant data
     */
    RestaurantResponseDTO createRestaurant(RestaurantRequestDTO requestDTO, User owner);

    /**
     * Update existing restaurant
     * @param id restaurant id
     * @param requestDTO details for updating a restaurant
     * @param owner owner of the restaurant
     * @return updated restaurant data
     */
    RestaurantResponseDTO updateRestaurant(Long id, RestaurantRequestDTO requestDTO, User owner);

    /**
     * Delete a restaurant
     * @param id restaurant id
     * @param owner owner of the restaurant
     */
    void deleteRestaurant(Long id, User owner);

    /**
     * gets all restaurants
     * @return list of all restaurants
     */
    List<RestaurantResponseDTO> getAllRestaurants();

    /**
     * get restaurants owned by a specific owner
     * @param owner restaurant owner
     * @return list of restaurants of that owner
     */
    List<RestaurantResponseDTO> getRestaurantsByOwner(User owner);
}
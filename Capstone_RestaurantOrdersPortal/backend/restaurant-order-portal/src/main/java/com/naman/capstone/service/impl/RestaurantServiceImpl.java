package com.naman.capstone.service.impl;

import com.naman.capstone.dto.request.RestaurantRequestDTO;
import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.exception.UnauthorizedException;
import com.naman.capstone.mapper.RestaurantMapper;
import com.naman.capstone.repository.RestaurantRepository;
import com.naman.capstone.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains logic for restaurant operations
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    /**
     * creates a new restaurant
     * @param requestDTO details of the restaurant to be created
     * @param owner user who will own the restaurant
     * @return created restaurant
     */
    @Override
    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO requestDTO, User owner) {

        logger.info("creating new restaurant with name: {}", requestDTO.getName());
        Restaurant restaurant = new Restaurant(requestDTO.getName(), requestDTO.getAddress(), requestDTO.getStatus(), owner);

        //save restaurant
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return RestaurantMapper.toDTO(savedRestaurant);
    }


    /**
     * updates an existing restaurant
     * @param id restaurant id
     * @param requestDTO the updated restaurant details
     * @param owner the user requesting the update
     * @return updated restaurant
     */
    @Override
    public RestaurantResponseDTO updateRestaurant(Long id, RestaurantRequestDTO requestDTO, User owner) {

        logger.info("updating restaurant with id: {}", id);

        //finding restaurant by id
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        //checking owner of restaurant
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            logger.warn("unauthorized update attempt on restaurant id: {} by user id: {}", id, owner.getId());
            throw new UnauthorizedException("You are not authorized to update this restaurant");
        }

        //update information
        restaurant.setName(requestDTO.getName());
        restaurant.setAddress(requestDTO.getAddress());
        restaurant.setStatus(requestDTO.getStatus());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.toDTO(updatedRestaurant);
    }


    /**
     * deletes a restaurant
     * @param id restaurant id
     * @param owner the user requesting the deletion
     */
    @Override
    public void deleteRestaurant(Long id, User owner) {
        logger.info("deleting restaurant with id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        //checking owner of restaurant
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            logger.warn("unauthorized delete attempt on restaurant id: {} by user id: {}", id, owner.getId());
            throw new UnauthorizedException("You are not authorized to delete this restaurant");
        }

        restaurantRepository.delete(restaurant);
    }


    /**
     * retrieves all restaurants
     * * @return list of all restaurants
     */
    @Override
    public List<RestaurantResponseDTO> getAllRestaurants() {
        logger.info("fetching all restaurants");

        return restaurantRepository.findAll()
                .stream()
                .map(restaurant -> RestaurantMapper.toDTO(restaurant))
                .collect(Collectors.toList());
    }


    /**
     * retrieves a restaurant by id
     * @param id the id of the restaurant to retrieve
     * @return the requested restaurant as a response DTO
     */
    @Override
    public RestaurantResponseDTO getRestaurantById(Long id) {
        logger.info("fetching restaurant with id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        return RestaurantMapper.toDTO(restaurant);
    }


    /**
     * retrieves all restaurants owned by a specific user
     * @param owner the user whose restaurants are to be retrieved
     * @return a list of restaurants owned by the user as response DTOs
     */
    @Override
    public List<RestaurantResponseDTO> getRestaurantsByOwner(User owner) {
        logger.info("fetching restaurants for owner with id: {}", owner.getId());

        return restaurantRepository.findByOwner(owner)
                .stream()
                .map(restaurant -> RestaurantMapper.toDTO(restaurant))
                .collect(Collectors.toList());
    }
}
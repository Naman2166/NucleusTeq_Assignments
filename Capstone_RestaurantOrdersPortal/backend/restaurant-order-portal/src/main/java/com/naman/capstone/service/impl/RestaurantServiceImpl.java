package com.naman.capstone.service.impl;

import com.naman.capstone.dto.request.RestaurantRequestDTO;
import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.RestaurantNotFoundException;
import com.naman.capstone.exception.UnauthorizedException;
import com.naman.capstone.mapper.RestaurantMapper;
import com.naman.capstone.repository.RestaurantRepository;
import com.naman.capstone.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains logic for restaurant operations
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    //create restaurant
    @Override
    public RestaurantResponseDTO createRestaurant(RestaurantRequestDTO requestDTO, User owner) {

        Restaurant restaurant = new Restaurant(requestDTO.getName(), requestDTO.getAddress(), requestDTO.getStatus(), owner);

        //save restaurant
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.toDTO(savedRestaurant);
    }


    //update restaurant
    @Override
    public RestaurantResponseDTO updateRestaurant(Long id, RestaurantRequestDTO requestDTO, User owner) {

        //finding restaurant by id
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));

        //checking owner of restaurant
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new UnauthorizedException("You are not authorized to update this restaurant");
        }

        //update information
        restaurant.setName(requestDTO.getName());
        restaurant.setAddress(requestDTO.getAddress());
        restaurant.setStatus(requestDTO.getStatus());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.toDTO(updatedRestaurant);
    }


    //delete Restaurant
    @Override
    public void deleteRestaurant(Long id, User owner) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));

        //checking owner of restaurant
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this restaurant");
        }

        restaurantRepository.delete(restaurant);
    }


    //get all restaurants
    @Override
    public List<RestaurantResponseDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurant -> RestaurantMapper.toDTO(restaurant))
                .collect(Collectors.toList());
    }


    //get restaurants of a specific owner
    @Override
    public List<RestaurantResponseDTO> getRestaurantsByOwner(User owner) {
        return restaurantRepository.findByOwner(owner)
                .stream()
                .map(restaurant -> RestaurantMapper.toDTO(restaurant))
                .collect(Collectors.toList());
    }
}
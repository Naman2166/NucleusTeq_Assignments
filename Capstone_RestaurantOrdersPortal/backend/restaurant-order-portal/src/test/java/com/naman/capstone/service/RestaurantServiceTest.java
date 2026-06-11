package com.naman.capstone.service;

import com.naman.capstone.dto.request.RestaurantRequestDTO;
import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.exception.UnauthorizedException;
import com.naman.capstone.repository.RestaurantRepository;
import com.naman.capstone.service.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RestaurantService.
 */
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    RestaurantRepository restaurantRepository;

    @InjectMocks
    RestaurantServiceImpl restaurantService;


    /**
     * Test create restaurant
     */
    @Test
    void create_restaurant_success() {
        User owner = new User();
        owner.setId(1L);

        RestaurantRequestDTO request = new RestaurantRequestDTO();
        request.setName("Test");
        request.setAddress("Bhopal");

        Restaurant saved = new Restaurant();
        saved.setId(1L);
        saved.setOwner(owner);

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(saved);
        RestaurantResponseDTO res = restaurantService.createRestaurant(request, owner);

        assertNotNull(res);
    }


    /**
     * Test update restaurant success
     */
    @Test
    void update_restaurant_success() {

        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        RestaurantRequestDTO request = new RestaurantRequestDTO();
        request.setName("Updated");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        RestaurantResponseDTO res = restaurantService.updateRestaurant(1L, request, owner);

        assertNotNull(res);
    }


    /**
     * Test update restaurant not found
     */
    @Test
    void update_restaurant_not_found() {

        User owner = new User();
        owner.setId(1L);

        when(restaurantRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> restaurantService.updateRestaurant(1L, new RestaurantRequestDTO(), owner));
    }

    /**
     * Test update unauthorized
     */
    @Test
    void update_restaurant_unauthorized() {

        User owner = new User(); owner.setId(1L);
        User other = new User(); other.setId(2L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        assertThrows(UnauthorizedException.class, () -> restaurantService.updateRestaurant(1L, new RestaurantRequestDTO(), other));
    }


    /**
     * Test delete success
     */
    @Test
    void delete_restaurant_success() {
        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        restaurantService.deleteRestaurant(1L, owner);

        verify(restaurantRepository).delete(restaurant);
    }

    /**
     * Test delete unauthorized
     */
    @Test
    void delete_restaurant_unauthorized() {

        User owner = new User(); owner.setId(1L);
        User other = new User(); other.setId(2L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        assertThrows(UnauthorizedException.class, () -> restaurantService.deleteRestaurant(1L, other));
    }

    /**
     * Test get all restaurants.
     */
    @Test
    void get_all_restaurants_success() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        List<RestaurantResponseDTO> list = restaurantService.getAllRestaurants();

        assertEquals(1, list.size());
    }


    /**
     * Test get restaurant by id.
     */
    @Test
    void get_restaurant_by_id_success() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        RestaurantResponseDTO res = restaurantService.getRestaurantById(1L);

        assertNotNull(res);
    }

    /**
     * Test get restaurant not found.
     */
    @Test
    void get_restaurant_by_id_not_found() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getRestaurantById(1L));
    }


    /**
     * Test get restaurants by owner.
     */
    @Test
    void get_restaurants_by_owner_success() {
        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        when(restaurantRepository.findByOwner(owner)).thenReturn(List.of(restaurant));
        List<RestaurantResponseDTO> list = restaurantService.getRestaurantsByOwner(owner);

        assertEquals(1, list.size());
    }
}
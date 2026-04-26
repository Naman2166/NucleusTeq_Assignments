package com.naman.capstone.controller;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.RestaurantRequestDTO;
import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.repository.UserRepository;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * This class handles all API requests related to restaurants
 */
@RestController
@RequestMapping(AppConstants.RESTAURANT_BASE_URL)
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final CurrentUserService currentUserService;

    public RestaurantController(RestaurantService restaurantService, CurrentUserService currentUserService){
        this.restaurantService = restaurantService;
        this.currentUserService = currentUserService;
    }


    /**
     * Creates a new restaurant
     * @param restaurantRequestDTO data for creating a restaurant
     * @param userDetails current authenticated user details
     * @return created restaurant details
     */
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO,
                                                                  @AuthenticationPrincipal UserDetails userDetails){
        //converting UserDetails to User entity
        User user = currentUserService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(restaurantRequestDTO, user));
    }


    /**
     * Updates an existing restaurant by id
     * @param id restaurant id
     * @param restaurantRequestDTO updated restaurant data
     * @param userDetails current authenticated user
     * @return updated restaurant data
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(@PathVariable Long id,
                                                  @Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO,
                                                  @AuthenticationPrincipal UserDetails userDetails){
        User user = currentUserService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateRestaurant(id, restaurantRequestDTO, user));
    }


    /**
     * deletes a restaurant by id
     * @param id restaurant id
     * @param userDetails current authenticated user details
     * @return restaurant deleted message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        User user = currentUserService.getCurrentUser(userDetails);
        restaurantService.deleteRestaurant(id, user);
        return ResponseEntity.status(HttpStatus.OK).body("Restaurant deleted successfully");
    }


    /**
     * get all restaurants
     * @return list of restaurants
     */
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants(){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getAllRestaurants());
    }


    /**
     * get restaurants of the specific user or owner
     * @param userDetails current authenticated user details
     * @return list of user's restaurants
     */
    @GetMapping(AppConstants.MY_RESTAURANTS)
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsByOwner(@AuthenticationPrincipal UserDetails userDetails){
        User user = currentUserService.getCurrentUser(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantsByOwner(user));
    }

}
package com.naman.capstone.controller;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.RestaurantRequestDTO;
import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.RestaurantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naman.capstone.constant.AppConstants.MY_RESTAURANTS;
import static com.naman.capstone.constant.AppConstants.RESTAURANT_ID;


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

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);


    /**
     * Creates a new restaurant
     * @param restaurantRequestDTO data for creating a restaurant
     * @param userDetails authenticated user details
     * @return created restaurant details
     */
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO,
                                                                  @AuthenticationPrincipal UserDetails userDetails){
        log.info("Create restaurant request received");
        User user = currentUserService.getCurrentUser(userDetails);
        RestaurantResponseDTO response = restaurantService.createRestaurant(restaurantRequestDTO, user);
        log.info("Restaurant created successfully by userId={}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Updates an existing restaurant by id
     * @param id restaurant id
     * @param restaurantRequestDTO updated restaurant data
     * @param userDetails authenticated user
     * @return updated restaurant data
     */
    @PutMapping(RESTAURANT_ID)
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(@PathVariable("restaurantId") Long id,
                                                  @Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO,
                                                  @AuthenticationPrincipal UserDetails userDetails){
        log.info("Update restaurant request received for restaurantId={}", id);
        User user = currentUserService.getCurrentUser(userDetails);
        RestaurantResponseDTO response = restaurantService.updateRestaurant(id, restaurantRequestDTO, user);
        log.info("Restaurant updated successfully for restaurantId={}", id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * deletes a restaurant by id
     * @param id restaurant id
     * @param userDetails authenticated user details
     * @return restaurant deleted message
     */
    @DeleteMapping(RESTAURANT_ID)
    public ResponseEntity<String> deleteRestaurant(@PathVariable("restaurantId") Long id, @AuthenticationPrincipal UserDetails userDetails){
        log.warn("Delete restaurant request received for restaurantId={}", id);
        User user = currentUserService.getCurrentUser(userDetails);
        restaurantService.deleteRestaurant(id, user);
        log.info("Restaurant deleted successfully for restaurantId={}", id);

        return ResponseEntity.status(HttpStatus.OK).body("Restaurant deleted successfully");
    }


    /**
     * get all restaurants
     * @return list of restaurants
     */
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants(){
        log.info("Fetching all restaurants");
        List<RestaurantResponseDTO> response = restaurantService.getAllRestaurants();
        log.info("Fetched {} restaurants", response.size());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * get restaurant by id
     * @return restaurant object
     */
    @GetMapping(RESTAURANT_ID)
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable("restaurantId") Long id) {
        log.info("Fetching restaurant for restaurantId={}", id);
        RestaurantResponseDTO response = restaurantService.getRestaurantById(id);
        log.info("Restaurant fetched successfully for restaurantId={}", id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * get restaurants of the specific user or owner
     * @param userDetails authenticated user details
     * @return list of user's restaurants
     */
    @GetMapping(MY_RESTAURANTS)
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsByOwner(@AuthenticationPrincipal UserDetails userDetails){
        log.info("Fetching restaurants for current user");
        User user = currentUserService.getCurrentUser(userDetails);
        List<RestaurantResponseDTO> response = restaurantService.getRestaurantsByOwner(user);
        log.info("Fetched all restaurants for userId={}", user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
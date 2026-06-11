package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.RestaurantRequestDTO;
import com.naman.capstone.dto.response.RestaurantResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.enums.RestaurantStatus;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.RestaurantService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * restaurant controller tests
 */
@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;



    /**
     * mock user
     */
    private User mockUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    /**
     * mock request dto
     */
    private RestaurantRequestDTO mockRequest() {
        RestaurantRequestDTO request = new RestaurantRequestDTO();
        request.setName("Test Restaurant");
        request.setAddress("Test Address");
        request.setStatus(RestaurantStatus.OPEN);
        return request;
    }

    /**
     * mock response dto
     */
    private RestaurantResponseDTO mockResponse() {
        RestaurantResponseDTO response = new RestaurantResponseDTO();
        response.setId(1L);
        response.setName("Test Restaurant");
        return response;
    }



    /**
     * testing create restaurant success
     */
    @Test
    @WithMockUser
    void create_restaurant_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        when(restaurantService.createRestaurant(any(), any())).thenReturn(mockResponse());

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing create restaurant validation failure
     */
    @Test
    @WithMockUser
    void create_restaurant_validation_failure() throws Exception {

        RestaurantRequestDTO request = new RestaurantRequestDTO();

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing update restaurant success
     */
    @Test
    @WithMockUser
    void update_restaurant_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        when(restaurantService.updateRestaurant(eq(1L), any(), any())).thenReturn(mockResponse());

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing update restaurant validation failure
     */
    @Test
    @WithMockUser
    void update_restaurant_validation_failure() throws Exception {

        RestaurantRequestDTO request = new RestaurantRequestDTO();

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing delete restaurant success
     */
    @Test
    @WithMockUser
    void delete_restaurant_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        doNothing().when(restaurantService).deleteRestaurant(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Restaurant deleted successfully"));
    }


    /**
     * testing delete restaurant failure
     */
    @Test
    @WithMockUser
    void delete_restaurant_failure() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        doThrow(new RuntimeException("error")).when(restaurantService)
                .deleteRestaurant(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isInternalServerError());
    }


    /**
     * testing get all restaurants success
     */
    @Test
    @WithMockUser
    void get_all_restaurants_success() throws Exception {

        when(restaurantService.getAllRestaurants())
                .thenReturn(List.of(mockResponse()));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * testing get restaurant by id success
     */
    @Test
    @WithMockUser
    void get_restaurant_by_id_success() throws Exception {

        when(restaurantService.getRestaurantById(eq(1L)))
                .thenReturn(mockResponse());

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing get restaurants by owner success
     */
    @Test
    @WithMockUser
    void get_restaurants_by_owner_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(restaurantService.getRestaurantsByOwner(any(User.class)))
                .thenReturn(List.of(mockResponse()));

        mockMvc.perform(get("/api/restaurants/my-restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * testing get restaurant by id not found
     */
    @Test
    @WithMockUser
    void get_restaurant_by_id_not_found() throws Exception {

        when(restaurantService.getRestaurantById(eq(1L)))
                .thenThrow(new RuntimeException("not found"));

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isInternalServerError());
    }

}
package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.CategoryRequestDTO;
import com.naman.capstone.dto.response.CategoryResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.CategoryService;
import com.naman.capstone.service.CurrentUserService;

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
 * category controller tests
 */
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

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
    private CategoryRequestDTO mockRequest() {
        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Pizza");
        request.setRestaurantId(1L);
        return request;
    }

    /**
     * mock response dto
     */
    private CategoryResponseDTO mockResponse() {
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(1L);
        response.setName("Pizza");
        return response;
    }



    /**
     * testing create category success
     */
    @Test
    @WithMockUser
    void create_category_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(categoryService.createCategory(any(CategoryRequestDTO.class), any(User.class)))
                .thenReturn(mockResponse());

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    /**
     * testing update category success
     */
    @Test
    @WithMockUser
    void update_category_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(categoryService.updateCategory(eq(1L), any(CategoryRequestDTO.class), any(User.class)))
                .thenReturn(mockResponse());

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing delete category success
     */
    @Test
    @WithMockUser
    void delete_category_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        doNothing().when(categoryService)
                .deleteCategory(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("category deleted successfully"));
    }


    /**
     * testing get categories by restaurant success
     */
    @Test
    @WithMockUser
    void get_categories_by_restaurant_success() throws Exception {

        when(categoryService.getCategoriesByRestaurant(eq(1L)))
                .thenReturn(List.of(mockResponse()));

        mockMvc.perform(get("/api/categories/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * testing create category validation failure when request is empty
     */
    @Test
    @WithMockUser
    void create_category_validation_failure() throws Exception {

        CategoryRequestDTO request = new CategoryRequestDTO();

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing update category when category not found
     */
    @Test
    @WithMockUser
    void update_category_not_found() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(categoryService.updateCategory(eq(1L), any(), any()))
                .thenThrow(new RuntimeException("category not found"));

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isInternalServerError());
    }

}
package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.MenuItemRequestDTO;
import com.naman.capstone.dto.response.MenuItemResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.MenuItemService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * menu item controller tests
 */
@WebMvcTest(MenuItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuItemService menuItemService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * creates mock user
     */
    private User mockUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    /**
     * creates mock request dto
     */
    private MenuItemRequestDTO mockRequest() {
        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setName("Pizza");
        request.setPrice(new BigDecimal("200"));
        request.setCategoryId(1L);
        request.setRestaurantId(1L);
        return request;
    }

    /**
     * creates mock response dto
     */
    private MenuItemResponseDTO mockResponse() {
        MenuItemResponseDTO response = new MenuItemResponseDTO();
        response.setId(1L);
        response.setName("Pizza");
        response.setPrice(new BigDecimal("200"));
        return response;
    }



    /**
     * testing create menu item success
     */
    @Test
    @WithMockUser
    void create_menu_item_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(menuItemService.createMenuItem(any(), any()))
                .thenReturn(mockResponse());

        mockMvc.perform(post("/api/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing update menu item success
     */
    @Test
    @WithMockUser
    void update_menu_item_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(menuItemService.updateMenuItem(eq(1L), any(), any()))
                .thenReturn(mockResponse());

        mockMvc.perform(put("/api/menu-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing delete menu item success
     */
    @Test
    @WithMockUser
    void delete_menu_item_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        doNothing().when(menuItemService).deleteMenuItem(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/menu-items/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Menu item deleted successfully with id: 1"));
    }


    /**
     * testing get menu items by restaurant success
     */
    @Test
    @WithMockUser
    void get_menu_items_by_restaurant_success() throws Exception {

        when(menuItemService.getMenuItemsByRestaurant(eq(1L)))
                .thenReturn(List.of(mockResponse()));

        mockMvc.perform(get("/api/menu-items/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * testing get menu items by category success
     */
    @Test
    @WithMockUser
    void get_menu_items_by_category_success() throws Exception {

        when(menuItemService.getMenuItemsByCategory(eq(1L))).thenReturn(List.of(mockResponse()));

        mockMvc.perform(get("/api/menu-items/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * testing create menu item validation failure
     */
    @Test
    @WithMockUser
    void create_menu_item_validation_failure() throws Exception {

        MenuItemRequestDTO request = new MenuItemRequestDTO();

        mockMvc.perform(post("/api/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing update menu item validation failure
     */
    @Test
    @WithMockUser
    void update_menu_item_validation_failure() throws Exception {

        MenuItemRequestDTO request = new MenuItemRequestDTO();

        mockMvc.perform(put("/api/menu-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing update menu item not found
     */
    @Test
    @WithMockUser
    void update_menu_item_not_found() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(menuItemService.updateMenuItem(eq(1L), any(), any()))
                .thenThrow(new RuntimeException("not found"));

        mockMvc.perform(put("/api/menu-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isInternalServerError());
    }

    /**
     * testing delete menu item failure
     */
    @Test
    @WithMockUser
    void delete_menu_item_failure() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        doThrow(new RuntimeException("error")).when(menuItemService)
                .deleteMenuItem(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/menu-items/1"))
                .andExpect(status().isInternalServerError());
    }


    /**
     * testing get menu items by restaurant empty
     */
    @Test
    @WithMockUser
    void get_menu_items_by_restaurant_empty() throws Exception {

        when(menuItemService.getMenuItemsByRestaurant(eq(1L))).thenReturn(List.of());

        mockMvc.perform(get("/api/menu-items/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    /**
     * testing get menu items by category empty
     */
    @Test
    @WithMockUser
    void get_menu_items_by_category_empty() throws Exception {

        when(menuItemService.getMenuItemsByCategory(eq(1L))).thenReturn(List.of());

        mockMvc.perform(get("/api/menu-items/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
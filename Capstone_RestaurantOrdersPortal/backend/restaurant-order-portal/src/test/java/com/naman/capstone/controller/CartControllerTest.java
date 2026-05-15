package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.CartRequestDTO;
import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.CartService;
import com.naman.capstone.service.CurrentUserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;

import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * cart controller tests
 */
@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

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
    private CartRequestDTO mockRequest() {
        CartRequestDTO requestDTO = new CartRequestDTO();
        requestDTO.setMenuItemId(1L);
        requestDTO.setQuantity(2);
        return requestDTO;
    }

    /**
     * mock response dto
     */
    private CartResponseDTO mockResponse() {
        CartResponseDTO responseDTO = new CartResponseDTO();
        responseDTO.setTotalPrice(new BigDecimal("200"));
        return responseDTO;
    }



    /**
     *  testing add item to cart success
     */
    @Test
    @WithMockUser
    void add_item_success() throws Exception {

        when(currentUserService.getCurrentUser(any()))
                .thenReturn(mockUser());
        when(cartService.addItem(any(User.class), eq(1L), eq(2)))
                .thenReturn(mockResponse());

        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(200));
    }


    /**
     * testing get cart details success
     */
    @Test
    @WithMockUser
    void get_cart_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(cartService.getCart(any(User.class)))
                .thenReturn(mockResponse());

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(200));
    }


    /**
     * testing remove item from cart success
     */
    @Test
    @WithMockUser
    void remove_item_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        doNothing().when(cartService)
                .removeItem(any(User.class), eq(1L));

        mockMvc.perform(delete("/api/cart/items/1"))
                .andExpect(status().isNoContent());
    }


    /**
     * testing clear cart success
     */
    @Test
    @WithMockUser
    void clear_cart_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());

        doNothing().when(cartService)
                .clearCart(any(User.class));

        mockMvc.perform(delete("/api/cart"))
                .andExpect(status().isNoContent());
    }


    /**
     * testing add item validation failure when request is empty
     */
    @Test
    @WithMockUser
    void add_item_validation_failure() throws Exception {

        CartRequestDTO request = new CartRequestDTO();

        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing add item validation failure for invalid quantity
     */
    @Test
    @WithMockUser
    void add_item_invalid_quantity() throws Exception {

        CartRequestDTO request = new CartRequestDTO();
        request.setMenuItemId(1L);
        request.setQuantity(0);

        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing remove item when service throws exception
     */
    @Test
    @WithMockUser
    void remove_item_failure() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        doThrow(new RuntimeException("error"))
                .when(cartService)
                .removeItem(any(User.class), eq(1L));

        mockMvc.perform(delete("/api/cart/items/1"))
                .andExpect(status().isInternalServerError());
    }


}
package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.UpdateCartItemRequestDTO;
import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.CartItemService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * cart item controller tests
 */
@WebMvcTest(CartItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartItemService cartItemService;

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
    private UpdateCartItemRequestDTO mockRequest() {
        UpdateCartItemRequestDTO request = new UpdateCartItemRequestDTO();
        request.setQuantity(3);
        return request;
    }

    /**
     * mock response dto
     */
    private CartResponseDTO mockResponse() {
        CartResponseDTO response = new CartResponseDTO();
        response.setTotalPrice(new BigDecimal(300));
        return response;
    }



    /**
     * testing successful update of cart item quantity
     */
    @Test
    @WithMockUser
    void update_quantity_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(cartItemService.updateQuantity(any(User.class), eq(1L), eq(3)))
                .thenReturn(mockResponse());

        mockMvc.perform(put("/api/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(300.0));
    }


    /**
     * testing update cart item quantity validation failure when request is empty
     */
    @Test
    @WithMockUser
    void update_quantity_validation_failure() throws Exception {

        UpdateCartItemRequestDTO request = new UpdateCartItemRequestDTO();

        mockMvc.perform(put("/api/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}
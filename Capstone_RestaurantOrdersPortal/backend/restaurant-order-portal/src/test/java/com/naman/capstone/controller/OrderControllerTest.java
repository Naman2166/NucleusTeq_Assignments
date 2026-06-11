package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.OrderRequestDTO;
import com.naman.capstone.dto.response.OrderResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.enums.OrderStatus;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.OrderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
 * Testing API related to orders
 */
@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }


    /**
     * testing place order success
     */
    @Test
    @WithMockUser
    void place_order_success() throws Exception {

        OrderRequestDTO request = new OrderRequestDTO();
        request.setAddressId(1L);
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(1L);

        when(currentUserService.getCurrentUser(any())).thenReturn(mockUser());
        when(orderService.placeOrder(any(User.class), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    /**
     * testing place order failure when address id is missing
     */
    @Test
    @WithMockUser
    void place_order_validation_failure() throws Exception {

        OrderRequestDTO request = new OrderRequestDTO();

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing get order by id success
     */
    @Test
    @WithMockUser
    void get_order_by_id_success() throws Exception {

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(1L);

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        when(orderService.getOrderById(any(User.class), eq(1L))).thenReturn(response);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk());
    }


    /**
     * testing get order by id when order not found
     */
    @Test
    @WithMockUser
    void get_order_by_id_not_found() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(orderService.getOrderById(any(User.class), eq(1L)))
                .thenThrow(new RuntimeException("order not found"));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isInternalServerError());
    }

    /**
     * testing get orders by user success
     */
    @Test
    @WithMockUser
    void get_user_orders_success() throws Exception {

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(1L);

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        when(orderService.getUserOrders(any(User.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    /**
     * testing user order cancellation success
     */
    @Test
    @WithMockUser
    void cancel_order_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        doNothing().when(orderService).cancelOrder(any(User.class), eq(1L));

        mockMvc.perform(put("/api/orders/1/cancel"))
                .andExpect(status().isNoContent());
    }


    /**
     * testing cancel order by owner
     */
    @Test
    @WithMockUser
    void cancel_order_by_owner_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        doNothing().when(orderService).cancelOrderByOwner(eq(1L), any(User.class));

        mockMvc.perform(put("/api/orders/1/cancel-by-owner"))
                .andExpect(status().isNoContent());
    }


    /**
     * testing get orders by restaurant
     */
    @Test
    @WithMockUser
    void get_orders_by_restaurant_success() throws Exception {

        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(1L);

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        when(orderService.getOrdersByRestaurant(eq(1L), any(User.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * testing update order status
     */
    @Test
    @WithMockUser
    void update_order_status_success() throws Exception {

        OrderResponseDTO response = new OrderResponseDTO();
        response.setStatus(OrderStatus.PENDING);

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());
        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.PENDING), any(User.class))).thenReturn(response);

        mockMvc.perform(put("/api/orders/1/status")
                        .param("status", "PENDING"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value("PENDING"));
    }


    /**
     * testing update order status with invalid status value
     */
    @Test
    @WithMockUser
    void update_order_status_invalid() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());

        mockMvc.perform(put("/api/orders/1/status")
                        .param("status", "INVALID_STATUS"))
                        .andExpect(status().isInternalServerError());
    }

}
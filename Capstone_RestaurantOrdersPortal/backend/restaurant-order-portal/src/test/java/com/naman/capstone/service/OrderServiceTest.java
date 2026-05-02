package com.naman.capstone.service;

import com.naman.capstone.dto.response.OrderResponseDTO;
import com.naman.capstone.entity.*;
import com.naman.capstone.enums.OrderStatus;
import com.naman.capstone.exception.EmptyCartException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.exception.UnauthorizedException;
import com.naman.capstone.repository.*;
import com.naman.capstone.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for OrderService.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    AddressRepository addressRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    OrderServiceImpl orderService;


    /**
     * Test successful order placement.
     */
    @Test
    void place_order_success() {

        User user = new User();
        user.setId(1L);
        user.setWalletBalance(new BigDecimal("1000"));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setName("Test Restaurant");

        MenuItem menuItem = new MenuItem();
        menuItem.setId(5L);
        menuItem.setRestaurant(restaurant);

        CartItem item = new CartItem();
        item.setMenuItem(menuItem);
        item.setUnitPrice(new BigDecimal("200"));
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>(List.of(item)));
        cart.setTotalPrice(new BigDecimal("400"));

        Address address = new Address();
        address.setId(1L);

        Order savedOrder = new Order();
        savedOrder.setId(100L);
        savedOrder.setRestaurant(restaurant);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        OrderResponseDTO response = orderService.placeOrder(user, 1L);

        assertNotNull(response);
        assertEquals(100L, response.getOrderId());
    }


    /**
     * Test order placement when address is not found.
     */
    @Test
    void place_order_address_not_found() {
        User user = new User();
        user.setId(1L);
        user.setWalletBalance(new BigDecimal("1000"));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setRestaurant(restaurant);

        CartItem item = new CartItem();
        item.setMenuItem(menuItem);
        item.setUnitPrice(new BigDecimal("100"));
        item.setQuantity(1);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(List.of(item)));
        cart.setTotalPrice(new BigDecimal("100"));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(user, 1L));
    }


    /**
     * Test order placement with empty cart.
     */
    @Test
    void place_order_empty_cart() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        assertThrows(EmptyCartException.class, () -> orderService.placeOrder(user, 1L));
    }


    /**
     * Test fetching order by ID.
     */
    @Test
    void get_order_by_id_success() {
        User user = new User();
        user.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test");

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderResponseDTO res = orderService.getOrderById(user, 1L);

        assertNotNull(res);
    }


    /**
     * Test fetching all orders of a user
     */
    @Test
    void get_user_orders_success() {
        User user = new User();
        user.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test");

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);

        when(orderRepository.findByUser(user)).thenReturn(List.of(order, order));
        List<OrderResponseDTO> list = orderService.getUserOrders(user);

        assertEquals(2, list.size());
    }

}
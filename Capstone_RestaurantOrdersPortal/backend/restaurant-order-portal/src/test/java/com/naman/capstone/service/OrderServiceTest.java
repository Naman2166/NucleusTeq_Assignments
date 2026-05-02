package com.naman.capstone.service;

import com.naman.capstone.dto.response.OrderResponseDTO;
import com.naman.capstone.entity.*;
import com.naman.capstone.enums.OrderStatus;
import com.naman.capstone.exception.*;
import com.naman.capstone.repository.*;
import com.naman.capstone.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
     * Test successful order placement
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
     * Test order placement when address is not found
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
     * Test order placement with empty cart
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
     * Test fetching order by ID
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


    /**
     * testing get order by id when order not found
     */
    @Test
    void get_order_by_id_not_found() {

        User user = new User();
        user.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.getOrderById(user, 1L));
    }


    /**
     * testing get order by id unauthorized access
     */
    @Test
    void get_order_by_id_unauthorized() {

        User user = new User();
        user.setId(1L);

        User anotherUser = new User();
        anotherUser.setId(2L);

        Order order = new Order();
        order.setUser(anotherUser);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(UnauthorizedException.class, () ->
                orderService.getOrderById(user, 1L));
    }


    /**
     * testing update order status success
     */
    @Test
    void update_order_status_success() {
        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwner(owner);

        Order order = new Order();
        order.setId(1L);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderResponseDTO response = orderService.updateOrderStatus(1L, OrderStatus.COMPLETED, owner);

        assertNotNull(response);
        assertEquals(OrderStatus.COMPLETED, response.getStatus());
    }

    /**
     * testing place order with multiple restaurants in cart
     */
    @Test
    void place_order_multiple_restaurants() {

        User user = new User();
        user.setId(1L);
        user.setWalletBalance(new BigDecimal("1000"));

        Restaurant r1 = new Restaurant();
        r1.setId(1L);
        Restaurant r2 = new Restaurant();
        r2.setId(2L);

        MenuItem m1 = new MenuItem();
        m1.setRestaurant(r1);
        MenuItem m2 = new MenuItem();
        m2.setRestaurant(r2);

        CartItem i1 = new CartItem();
        i1.setMenuItem(m1);
        i1.setUnitPrice(new BigDecimal("100"));
        i1.setQuantity(1);

        CartItem i2 = new CartItem();
        i2.setMenuItem(m2);
        i2.setUnitPrice(new BigDecimal("100"));
        i2.setQuantity(1);

        Cart cart = new Cart();
        cart.setItems(List.of(i1, i2));
        cart.setTotalPrice(new BigDecimal("200"));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        assertThrows(MultipleRestaurantCartException.class, () -> orderService.placeOrder(user, 1L));
    }


    /**
     * testing place order with insufficient wallet balance
     */
    @Test
    void place_order_insufficient_balance() {

        User user = new User();
        user.setId(1L);
        user.setWalletBalance(new BigDecimal("50"));

        Restaurant r = new Restaurant();
        r.setId(1L);
        MenuItem m = new MenuItem();
        m.setRestaurant(r);

        CartItem item = new CartItem();
        item.setMenuItem(m);
        item.setUnitPrice(new BigDecimal("100"));
        item.setQuantity(1);

        Cart cart = new Cart();
        cart.setItems(List.of(item));
        cart.setTotalPrice(new BigDecimal("100"));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        assertThrows(InsufficientBalanceException.class, () -> orderService.placeOrder(user, 1L));
    }


    /**
     * testing cancel order after time exceeded ie after 30 seconds
     */
    @Test
    void cancel_order_time_exceeded() {

        User user = new User();
        user.setId(1L);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setOrderTime(LocalDateTime.now().minusMinutes(1));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(OrderCancellationTimeExceededException.class, () -> orderService.cancelOrder(user, 1L));
    }


    /**
     * testing cancel order when status not cancellable
     */
    @Test
    void cancel_order_invalid_status() {

        User user = new User(); user.setId(1L);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(OrderNotCancellableException.class, () -> orderService.cancelOrder(user, 1L));
    }

}
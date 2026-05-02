package com.naman.capstone.entity;

import com.naman.capstone.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * order entity test
 */
class OrderTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        User user = new User();
        user.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);

        Address address = new Address();
        address.setId(3L);

        Order order = new Order(user, restaurant, address);

        assertEquals(user, order.getUser());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(address, order.getAddress());
        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertEquals(BigDecimal.ZERO, order.getTotalPrice());
        assertNotNull(order.getOrderTime());
    }


    /**
     * testing add item functionality
     */
    @Test
    void test_add_item() {
        OrderItem item = new OrderItem();
        item.setUnitPrice(new BigDecimal("100"));

        Order order = new Order();
        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(order, item.getOrder());
    }


    /**
     * testing remove item functionality
     */
    @Test
    void test_remove_item() {
        OrderItem item = new OrderItem();
        item.setUnitPrice(new BigDecimal("50"));
        item.setQuantity(2);

        Order order = new Order();
        order.addItem(item);
        order.removeItem(item);

        assertTrue(order.getItems().isEmpty());
        assertNull(item.getOrder());
    }


    /**
     * testing calculate total price
     */
    @Test
    void test_calculate_total() {
        OrderItem item1 = new OrderItem();
        item1.setUnitPrice(new BigDecimal("50"));
        item1.setQuantity(2);

        OrderItem item2 = new OrderItem();
        item2.setUnitPrice(new BigDecimal("100"));
        item2.setQuantity(2);

        Order order = new Order();
        order.addItem(item1);
        order.addItem(item2);

        order.calculateTotal();

        assertEquals(new BigDecimal("300"), order.getTotalPrice());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        User user = new User();
        user.setId(10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(20L);

        Address address = new Address();
        address.setId(30L);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setAddress(address);
        order.setStatus(OrderStatus.DELIVERED);

        assertEquals(1L, order.getId());
        assertEquals(user, order.getUser());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(address, order.getAddress());
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }
}
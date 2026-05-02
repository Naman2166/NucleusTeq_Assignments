package com.naman.capstone.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * order item entity test
 */
class OrderItemTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        Order order = new Order();
        order.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(2L);

        OrderItem orderItem = new OrderItem(order, menuItem, 2, new BigDecimal("100"));

        assertEquals(order, orderItem.getOrder());
        assertEquals(menuItem, orderItem.getMenuItem());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(new BigDecimal("100"), orderItem.getUnitPrice());
        assertEquals(new BigDecimal("200"), orderItem.getTotalPrice());
    }


    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        OrderItem orderItem = new OrderItem();

        assertNull(orderItem.getOrder());
        assertNull(orderItem.getMenuItem());
        assertEquals(0, orderItem.getQuantity());
        assertNull(orderItem.getUnitPrice());
        assertNull(orderItem.getTotalPrice());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        Order order = new Order();
        order.setId(3L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(4L);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setUnitPrice(new BigDecimal("50"));
        orderItem.setQuantity(3);

        assertEquals(order, orderItem.getOrder());
        assertEquals(menuItem, orderItem.getMenuItem());
        assertEquals(3, orderItem.getQuantity());
        assertEquals(new BigDecimal("50"), orderItem.getUnitPrice());
        assertEquals(new BigDecimal("150"), orderItem.getTotalPrice());
    }


    /**
     * testing calculate total price method
     */
    @Test
    void test_calculate_total_price() {
        OrderItem orderItem = new OrderItem();
        orderItem.setUnitPrice(new BigDecimal("20"));
        orderItem.setQuantity(5);
        orderItem.calculateTotalPrice();

        assertEquals(new BigDecimal("100"), orderItem.getTotalPrice());
    }


    /**
     * testing total price recalculation method upon quantity change
     */
    @Test
    void test_update_quantity() {
        OrderItem orderItem = new OrderItem();
        orderItem.setUnitPrice(new BigDecimal("10"));
        orderItem.setQuantity(2);
        orderItem.setQuantity(5);

        assertEquals(new BigDecimal("50"), orderItem.getTotalPrice());
    }


    /**
     * testing total price recalculation method upon unitPrice change
     */
    @Test
    void test_update_unit_price() {
        OrderItem orderItem = new OrderItem();
        orderItem.setUnitPrice(new BigDecimal("10"));
        orderItem.setQuantity(2);
        orderItem.setUnitPrice(new BigDecimal("20"));

        assertEquals(new BigDecimal("40"), orderItem.getTotalPrice());
    }
}
package com.naman.capstone.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * cart entity test
 */
class CartTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart(user);

        assertEquals(user, cart.getUser());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }


    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        Cart cart = new Cart();

        assertNull(cart.getUser());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        User user = new User();
        user.setId(2L);

        Cart cart = new Cart();
        cart.setId(10L);
        cart.setUser(user);
        cart.setTotalPrice(new BigDecimal("100"));

        assertEquals(10L, cart.getId());
        assertEquals(user, cart.getUser());
        assertEquals(new BigDecimal("100"), cart.getTotalPrice());
    }


    /**
     * testing add item functionality
     */
    @Test
    void test_add_item() {
        CartItem item = new CartItem();
        item.setUnitPrice(new BigDecimal("10"));
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.addItem(item);

        assertEquals(1, cart.getItems().size());
        assertEquals(cart, item.getCart());
    }


    /**
     * testing remove item functionality
     */
    @Test
    void test_remove_item() {
        CartItem item = new CartItem();
        item.setUnitPrice(new BigDecimal("10"));
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.addItem(item);
        cart.removeItem(item);

        assertTrue(cart.getItems().isEmpty());
        assertNull(item.getCart());
    }
}
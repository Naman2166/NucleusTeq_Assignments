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

    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {

        Cart c1 = new Cart();
        Cart c2 = new Cart();

        c1.setId(1L);
        c2.setId(1L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_negative() {
        Cart c1 = new Cart();
        Cart c2 = new Cart();
        c1.setId(1L);
        c2.setId(2L);

        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }


    /**
     * testing toString
     */
    @Test
    void test_to_string() {
        Cart cart = new Cart();
        cart.setId(1L);

        assertNotNull(cart.toString());
    }
}
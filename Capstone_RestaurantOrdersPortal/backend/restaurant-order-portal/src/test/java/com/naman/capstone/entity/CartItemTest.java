package com.naman.capstone.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * cart item entity test
 */
class CartItemTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        Cart cart = new Cart();
        cart.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(2L);

        CartItem cartItem = new CartItem(cart, menuItem, 2, new BigDecimal("100"));

        assertEquals(cart, cartItem.getCart());
        assertEquals(menuItem, cartItem.getMenuItem());
        assertEquals(2, cartItem.getQuantity());
        assertEquals(new BigDecimal("100"), cartItem.getUnitPrice());
        assertEquals(new BigDecimal("200"), cartItem.getTotalPrice());
    }


    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        CartItem cartItem = new CartItem();

        assertNull(cartItem.getCart());
        assertNull(cartItem.getMenuItem());
        assertEquals(0, cartItem.getQuantity());
        assertNull(cartItem.getUnitPrice());
        assertNull(cartItem.getTotalPrice());
    }


    /**
     * testing total price calculation
     */
    @Test
    void test_calculate_total_price() {
        CartItem cartItem = new CartItem();
        cartItem.setUnitPrice(new BigDecimal("20"));
        cartItem.setQuantity(5);

        assertEquals(new BigDecimal("100"), cartItem.getTotalPrice());
    }


    /**
     * testing update quantity recalculates total price
     */
    @Test
    void test_update_quantity() {
        CartItem cartItem = new CartItem();
        cartItem.setUnitPrice(new BigDecimal("10"));
        cartItem.setQuantity(2);
        cartItem.setQuantity(5);

        assertEquals(new BigDecimal("50"), cartItem.getTotalPrice());
    }


    /**
     * testing update unit price recalculates total price
     */
    @Test
    void test_update_unit_price() {
        CartItem cartItem = new CartItem();
        cartItem.setUnitPrice(new BigDecimal("10"));
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(new BigDecimal("20"));

        assertEquals(new BigDecimal("40"), cartItem.getTotalPrice());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        CartItem cartItem = new CartItem();

        Cart cart = new Cart();
        cart.setId(3L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(4L);

        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);

        cartItem.setUnitPrice(new BigDecimal("50"));
        cartItem.setQuantity(3);
        assertEquals(cart, cartItem.getCart());
        assertEquals(menuItem, cartItem.getMenuItem());
        assertEquals(3, cartItem.getQuantity());
        assertEquals(new BigDecimal("50"), cartItem.getUnitPrice());
        assertEquals(new BigDecimal("150"), cartItem.getTotalPrice());
    }


    /**
     * testing equals and hashcode
     */
    @Test
    void test_equals_and_hashcode() {
        Cart cart = new Cart();
        MenuItem menuItem = new MenuItem();

        CartItem item1 = new CartItem(cart, menuItem, 2, new BigDecimal("100"));
        CartItem item2 = new CartItem(cart, menuItem, 2, new BigDecimal("100"));
        item1.setId(1L);
        item2.setId(1L);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    /**
     * testing equals and hashcode for different objects
     */
    @Test
    void test_equals_and_hashcode_negative() {

        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        item1.setId(1L);
        item2.setId(2L);

        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }


    /**
     * test toString
     */
    @Test
    void test_to_string() {
        CartItem item = new CartItem();
        item.setId(1L);

        String result = item.toString();
        assertNotNull(result);
    }

}
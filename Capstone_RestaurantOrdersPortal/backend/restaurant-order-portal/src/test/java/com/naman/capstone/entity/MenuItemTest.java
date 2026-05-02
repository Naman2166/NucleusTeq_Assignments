package com.naman.capstone.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * menu item entity test
 */
class MenuItemTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        Category category = new Category();
        category.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);

        MenuItem menuItem = new MenuItem("Pizza", new BigDecimal("200"), category, restaurant, "img.jpg");

        assertEquals("Pizza", menuItem.getName());
        assertEquals(new BigDecimal("200"), menuItem.getPrice());
        assertEquals(category, menuItem.getCategory());
        assertEquals(restaurant, menuItem.getRestaurant());
        assertEquals("img.jpg", menuItem.getImageUrl());
    }


    /**
     * testing entity with null category and restaurant
     */
    @Test
    void test_null_relations() {
        MenuItem menuItem = new MenuItem("Burger", new BigDecimal("100"), null, null, null);
        assertEquals("Burger", menuItem.getName());
        assertNull(menuItem.getCategory());
        assertNull(menuItem.getRestaurant());
    }


    /**
     * testing entity with partial data
     */
    @Test
    void test_partial_data() {
        MenuItem menuItem = new MenuItem();

        menuItem.setName("Pasta");
        menuItem.setPrice(new BigDecimal("150"));

        assertEquals("Pasta", menuItem.getName());
        assertEquals(new BigDecimal("150"), menuItem.getPrice());
        assertNull(menuItem.getCategory());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        MenuItem menuItem = new MenuItem();

        Category category = new Category();
        category.setId(3L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(4L);

        menuItem.setId(10L);
        menuItem.setName("Sandwich");
        menuItem.setPrice(new BigDecimal("50"));
        menuItem.setCategory(category);
        menuItem.setRestaurant(restaurant);
        menuItem.setImageUrl("img2.jpg");

        assertEquals(10L, menuItem.getId());
        assertEquals("Sandwich", menuItem.getName());
        assertEquals(new BigDecimal("50"), menuItem.getPrice());
        assertEquals(category, menuItem.getCategory());
        assertEquals(restaurant, menuItem.getRestaurant());
        assertEquals("img2.jpg", menuItem.getImageUrl());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        MenuItem m1 = new MenuItem();
        MenuItem m2 = new MenuItem();
        m1.setId(1L);
        m2.setId(1L);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_negative() {
        MenuItem m1 = new MenuItem();
        MenuItem m2 = new MenuItem();
        m1.setId(1L);
        m2.setId(2L);

        assertNotEquals(m1, m2);
        assertNotEquals(m1.hashCode(), m2.hashCode());
    }


    /**
     * testing toString
     */
    @Test
    void test_to_string() {
        MenuItem item = new MenuItem();
        item.setId(1L);

        assertNotNull(item.toString());
    }
}
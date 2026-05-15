package com.naman.capstone.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * category entity test
 */
class CategoryTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category category = new Category("Pizza", restaurant);

        assertEquals("Pizza", category.getName());
        assertEquals(restaurant, category.getRestaurant());
    }


    /**
     * testing entity with null restaurant
     */
    @Test
    void test_null_restaurant() {
        Category category = new Category("Burger", null);
        assertEquals("Burger", category.getName());
        assertNull(category.getRestaurant());
    }


    /**
     * testing entity with partial data
     */
    @Test
    void test_partial_data() {
        Category category = new Category();
        category.setName("Drinks");

        assertEquals("Drinks", category.getName());
        assertNull(category.getRestaurant());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        Category category = new Category();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);

        category.setId(10L);
        category.setName("Dessert");
        category.setRestaurant(restaurant);

        assertEquals(10L, category.getId());
        assertEquals("Dessert", category.getName());
        assertEquals(restaurant, category.getRestaurant());
    }


    /**
     * testing equals and hashcode for identical objects
     */
    @Test
    void test_equals_and_hashcode() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category c1 = new Category("Pizza", restaurant);
        Category c2 = new Category("Pizza", restaurant);
        c1.setId(1L);
        c2.setId(1L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    /**
     * testing equals and hashcode for different objects
     */
    @Test
    void test_equals_and_hashcode_negative() {
        Category c1 = new Category();
        Category c2 = new Category();
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
        Category category = new Category();
        category.setId(1L);
        category.setName("Pizza");

        String result = category.toString();
        assertNotNull(result);
    }

}
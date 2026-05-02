package com.naman.capstone.entity;

import com.naman.capstone.enums.RestaurantStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * restaurant entity test
 */
class RestaurantTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant("Dominos", "AB Road", RestaurantStatus.OPEN, owner, "img.jpg");

        assertEquals("Dominos", restaurant.getName());
        assertEquals("AB Road", restaurant.getAddress());
        assertEquals(RestaurantStatus.OPEN, restaurant.getStatus());
        assertEquals(owner, restaurant.getOwner());
        assertEquals("img.jpg", restaurant.getImageUrl());
    }


    /**
     * testing entity with null owner
     */
    @Test
    void test_null_owner() {
        Restaurant restaurant = new Restaurant("Pizza Hut", "Vijay nagar", RestaurantStatus.CLOSED, null, null);
        assertEquals("Pizza Hut", restaurant.getName());
        assertNull(restaurant.getOwner());
    }


    /**
     * testing entity with partial data
     */
    @Test
    void test_partial_data() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Burger King");
        restaurant.setAddress("Vijay Nagar");

        assertEquals("Burger King", restaurant.getName());
        assertEquals("Vijay Nagar", restaurant.getAddress());
        assertNull(restaurant.getOwner());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        Restaurant restaurant = new Restaurant();

        User owner = new User();
        owner.setId(2L);

        restaurant.setId(10L);
        restaurant.setName("Burger King");
        restaurant.setAddress("Indore");
        restaurant.setStatus(RestaurantStatus.OPEN);
        restaurant.setOwner(owner);
        restaurant.setImageUrl("img2.jpg");

        assertEquals(10L, restaurant.getId());
        assertEquals("Burger King", restaurant.getName());
        assertEquals("Indore", restaurant.getAddress());
        assertEquals(RestaurantStatus.OPEN, restaurant.getStatus());
        assertEquals(owner, restaurant.getOwner());
        assertEquals("img2.jpg", restaurant.getImageUrl());
    }

    
    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        Restaurant r1 = new Restaurant();
        Restaurant r2 = new Restaurant();
        r1.setId(1L);
        r2.setId(1L);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_negative() {
        Restaurant r1 = new Restaurant();
        Restaurant r2 = new Restaurant();
        r1.setId(1L);
        r2.setId(2L);

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }


    /**
     * testing toString
     */
    @Test
    void test_to_string() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        assertNotNull(restaurant.toString());
    }
}
package com.naman.capstone.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * menu item response dto test
 */
class MenuItemResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        MenuItemResponseDTO dto = new MenuItemResponseDTO(1L, "Pizza", new BigDecimal("250"), 10L, 100L, "image.jpg");

        assertEquals(1L, dto.getId());
        assertEquals("Pizza", dto.getName());
        assertEquals(new BigDecimal("250"), dto.getPrice());
        assertEquals(10L, dto.getCategoryId());
        assertEquals(100L, dto.getRestaurantId());
        assertEquals("image.jpg", dto.getImageUrl());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        MenuItemResponseDTO dto = new MenuItemResponseDTO();
        dto.setId(2L);
        dto.setName("Burger");
        dto.setPrice(new BigDecimal("150"));
        dto.setCategoryId(20L);
        dto.setRestaurantId(200L);
        dto.setImageUrl("burger.jpg");

        assertEquals(2L, dto.getId());
        assertEquals("Burger", dto.getName());
        assertEquals(new BigDecimal("150"), dto.getPrice());
        assertEquals(20L, dto.getCategoryId());
        assertEquals(200L, dto.getRestaurantId());
        assertEquals("burger.jpg", dto.getImageUrl());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        MenuItemResponseDTO m1 = new MenuItemResponseDTO(1L, "Pizza", new BigDecimal("250"), 10L, 100L, "img.jpg");
        MenuItemResponseDTO m2 = new MenuItemResponseDTO(1L, "Pizza", new BigDecimal("250"), 10L, 100L, "img.jpg");
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        MenuItemResponseDTO m1 = new MenuItemResponseDTO(1L, "Pizza", new BigDecimal("250"), 10L, 100L, "img.jpg");
        MenuItemResponseDTO m2 = new MenuItemResponseDTO(2L, "Burger", new BigDecimal("150"), 20L, 200L, "img2.jpg");
        assertNotEquals(m1, m2);
        assertNotEquals(m1.hashCode(), m2.hashCode());
    }


    /**
     * testing equals with null and different class
     */
    @Test
    void test_equals_with_null_and_different_class() {
        MenuItemResponseDTO dto = new MenuItemResponseDTO();
        dto.setId(1L);
        assertNotEquals(dto, null);
        assertNotEquals(dto, new Object());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        MenuItemResponseDTO dto = new MenuItemResponseDTO(1L, "Pizza", new BigDecimal("250"), 10L, 100L, "image.jpg");
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Pizza"));
        assertTrue(result.contains("250"));
    }
}
package com.naman.capstone.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * category response dto test
 */
class CategoryResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        CategoryResponseDTO dto = new CategoryResponseDTO(1L, "Pizza", 10L);
        assertEquals(1L, dto.getId());
        assertEquals("Pizza", dto.getName());
        assertEquals(10L, dto.getRestaurantId());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(2L);
        dto.setName("Burger");
        dto.setRestaurantId(20L);

        assertEquals(2L, dto.getId());
        assertEquals("Burger", dto.getName());
        assertEquals(20L, dto.getRestaurantId());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        CategoryResponseDTO c1 = new CategoryResponseDTO(1L, "Pizza", 10L);
        CategoryResponseDTO c2 = new CategoryResponseDTO(1L, "Pizza", 10L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        CategoryResponseDTO c1 = new CategoryResponseDTO(1L, "Pizza", 10L);
        CategoryResponseDTO c2 = new CategoryResponseDTO(2L, "Burger", 10L);

        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }


    /**
     * testing equals with null and different class
     */
    @Test
    void test_equals_with_null_and_different_class() {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(1L);

        assertNotEquals(dto, null);
        assertNotEquals(dto, new Object());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        CategoryResponseDTO dto = new CategoryResponseDTO(1L, "Pizza", 10L);
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Pizza"));
        assertTrue(result.contains("10"));
    }
}
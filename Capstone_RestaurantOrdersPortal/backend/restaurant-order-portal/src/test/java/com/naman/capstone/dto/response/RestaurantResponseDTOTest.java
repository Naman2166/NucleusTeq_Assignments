package com.naman.capstone.dto.response;

import com.naman.capstone.enums.RestaurantStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * restaurant response dto test
 */
class RestaurantResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        RestaurantResponseDTO dto = new RestaurantResponseDTO(1L, "Dominos", "AB Road, Indore","fast food restaurant", RestaurantStatus.OPEN, "image.jpg");

        assertEquals(1L, dto.getId());
        assertEquals("Dominos", dto.getName());
        assertEquals("AB Road, Indore", dto.getAddress());
        assertEquals("fast food restaurant", dto.getDescription());
        assertEquals(RestaurantStatus.OPEN, dto.getStatus());
        assertEquals("image.jpg", dto.getImageUrl());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        RestaurantResponseDTO dto = new RestaurantResponseDTO();
        dto.setId(2L);
        dto.setName("Burger King");
        dto.setAddress("Vijay Nagar, Indore");
        dto.setDescription("fast food restaurant");
        dto.setStatus(RestaurantStatus.CLOSED);
        dto.setImageUrl("burger.jpg");

        assertEquals(2L, dto.getId());
        assertEquals("Burger King", dto.getName());
        assertEquals("Vijay Nagar, Indore", dto.getAddress());
        assertEquals("fast food restaurant", dto.getDescription());
        assertEquals(RestaurantStatus.CLOSED, dto.getStatus());
        assertEquals("burger.jpg", dto.getImageUrl());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        RestaurantResponseDTO r1 = new RestaurantResponseDTO(1L, "Dominos", "AB Road","fast food restaurant", RestaurantStatus.OPEN, "img.jpg");
        RestaurantResponseDTO r2 = new RestaurantResponseDTO(1L, "Dominos", "AB Road","fast food restaurant", RestaurantStatus.OPEN, "img.jpg");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        RestaurantResponseDTO r1 = new RestaurantResponseDTO(1L, "Dominos", "AB Road","fast food restaurant", RestaurantStatus.OPEN, "img.jpg");
        RestaurantResponseDTO r2 = new RestaurantResponseDTO(2L, "Burger King", "Vijay Nagar","fast food restaurant", RestaurantStatus.CLOSED, "img2.jpg");

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        RestaurantResponseDTO dto = new RestaurantResponseDTO(1L, "Dominos", "AB Road","fast food restaurant", RestaurantStatus.OPEN, "image.jpg");
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Dominos"));
        assertTrue(result.contains("AB Road"));
        assertTrue(result.contains("OPEN"));
    }
}
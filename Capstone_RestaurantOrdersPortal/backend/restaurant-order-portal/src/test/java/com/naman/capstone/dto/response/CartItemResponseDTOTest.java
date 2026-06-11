package com.naman.capstone.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * cart item response dto test
 */
class CartItemResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        CartItemResponseDTO dto = new CartItemResponseDTO(1L, 10L, "Pizza", 2, new BigDecimal("500"));
        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getMenuItemId());
        assertEquals("Pizza", dto.getMenuItemName());
        assertEquals(2, dto.getQuantity());
        assertEquals(new BigDecimal("500"), dto.getTotalPrice());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(2L);
        dto.setMenuItemId(20L);
        dto.setMenuItemName("Burger");
        dto.setQuantity(3);
        dto.setTotalPrice(new BigDecimal("300"));

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getMenuItemId());
        assertEquals("Burger", dto.getMenuItemName());
        assertEquals(3, dto.getQuantity());
        assertEquals(new BigDecimal("300"), dto.getTotalPrice());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        CartItemResponseDTO c1 = new CartItemResponseDTO();
        CartItemResponseDTO c2 = new CartItemResponseDTO();
        c1.setId(1L);
        c2.setId(1L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        CartItemResponseDTO c1 = new CartItemResponseDTO();
        CartItemResponseDTO c2 = new CartItemResponseDTO();
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
        CartItemResponseDTO dto = new CartItemResponseDTO(1L, 10L, "Pizza", 2, new BigDecimal("500"));
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Pizza"));
    }
}
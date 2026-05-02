package com.naman.capstone.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * order item response dto test
 */
class OrderItemResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        OrderItemResponseDTO dto = new OrderItemResponseDTO(1L, 10L, "Pizza", 2, new BigDecimal("250"), new BigDecimal("500"));

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getMenuItemId());
        assertEquals("Pizza", dto.getMenuItemName());
        assertEquals(2, dto.getQuantity());
        assertEquals(new BigDecimal("250"), dto.getUnitPrice());
        assertEquals(new BigDecimal("500"), dto.getTotalPrice());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(2L);
        dto.setMenuItemId(20L);
        dto.setMenuItemName("Burger");
        dto.setQuantity(3);
        dto.setUnitPrice(new BigDecimal("150"));
        dto.setTotalPrice(new BigDecimal("450"));

        assertEquals(2L, dto.getId());
        assertEquals(20L, dto.getMenuItemId());
        assertEquals("Burger", dto.getMenuItemName());
        assertEquals(3, dto.getQuantity());
        assertEquals(new BigDecimal("150"), dto.getUnitPrice());
        assertEquals(new BigDecimal("450"), dto.getTotalPrice());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        OrderItemResponseDTO o1 = new OrderItemResponseDTO();
        OrderItemResponseDTO o2 = new OrderItemResponseDTO();
        o1.setId(1L);
        o2.setId(1L);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        OrderItemResponseDTO o1 = new OrderItemResponseDTO();
        OrderItemResponseDTO o2 = new OrderItemResponseDTO();
        o1.setId(1L);
        o2.setId(2L);

        assertNotEquals(o1, o2);
        assertNotEquals(o1.hashCode(), o2.hashCode());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        OrderItemResponseDTO dto = new OrderItemResponseDTO(1L, 10L, "Pizza", 2, new BigDecimal("250"), new BigDecimal("500"));
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Pizza"));
        assertTrue(result.contains("500"));
    }
}
package com.naman.capstone.dto.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * cart response dto test
 */
class CartResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        CartItemResponseDTO item = new CartItemResponseDTO(1L, 10L, "Pizza", 2, new BigDecimal("500"));
        CartResponseDTO dto = new CartResponseDTO(100L, new BigDecimal("500"), List.of(item));

        assertEquals(100L, dto.getCartId());
        assertEquals(new BigDecimal("500"), dto.getTotalPrice());
        assertEquals(1, dto.getItems().size());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        CartResponseDTO dto = new CartResponseDTO();

        CartItemResponseDTO item = new CartItemResponseDTO();
        item.setId(1L);

        dto.setCartId(200L);
        dto.setTotalPrice(new BigDecimal("800"));
        dto.setItems(List.of(item));

        assertEquals(200L, dto.getCartId());
        assertEquals(new BigDecimal("800"), dto.getTotalPrice());
        assertEquals(1, dto.getItems().size());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        CartResponseDTO c1 = new CartResponseDTO();
        CartResponseDTO c2 = new CartResponseDTO();
        c1.setCartId(1L);
        c2.setCartId(1L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        CartResponseDTO c1 = new CartResponseDTO();
        CartResponseDTO c2 = new CartResponseDTO();
        c1.setCartId(1L);
        c2.setCartId(2L);

        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }


    /**
     * testing equals with null and different class
     */
    @Test
    void test_equals_with_null_and_different_class() {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartId(1L);

        assertNotEquals(dto, null);
        assertNotEquals(dto, new Object());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {

        CartItemResponseDTO item = new CartItemResponseDTO(1L, 10L, "Pizza", 2, new BigDecimal("500"));
        CartResponseDTO dto = new CartResponseDTO(100L, new BigDecimal("500"), List.of(item));
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("100"));
        assertTrue(result.contains("500"));
    }
}
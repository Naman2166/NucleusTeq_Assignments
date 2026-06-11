package com.naman.capstone.dto.response;

import com.naman.capstone.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * order response dto test
 */
class OrderResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        OrderItemResponseDTO item = new OrderItemResponseDTO(
                1L, 10L, "Pizza", 2,
                new BigDecimal("250"), new BigDecimal("500")
        );

        LocalDateTime time = LocalDateTime.now();
        OrderResponseDTO dto = new OrderResponseDTO(
                100L, "Dominos", new BigDecimal("500"),
                OrderStatus.PLACED, time, List.of(item)
        );

        assertEquals(100L, dto.getOrderId());
        assertEquals("Dominos", dto.getRestaurantName());
        assertEquals(new BigDecimal("500"), dto.getTotalPrice());
        assertEquals(OrderStatus.PLACED, dto.getStatus());
        assertEquals(time, dto.getOrderTime());
        assertEquals(1, dto.getItems().size());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        OrderResponseDTO dto = new OrderResponseDTO();
        OrderItemResponseDTO item = new OrderItemResponseDTO();
        item.setId(1L);

        dto.setOrderId(200L);
        dto.setRestaurantName("Burger King");
        dto.setTotalPrice(new BigDecimal("800"));
        dto.setStatus(OrderStatus.DELIVERED);
        dto.setOrderTime(LocalDateTime.now());
        dto.setItems(List.of(item));

        assertEquals(200L, dto.getOrderId());
        assertEquals("Burger King", dto.getRestaurantName());
        assertEquals(new BigDecimal("800"), dto.getTotalPrice());
        assertEquals(OrderStatus.DELIVERED, dto.getStatus());
        assertEquals(LocalDateTime.now(), dto.getOrderTime());
        assertEquals(1, dto.getItems().size());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        OrderResponseDTO o1 = new OrderResponseDTO();
        OrderResponseDTO o2 = new OrderResponseDTO();
        o1.setOrderId(1L);
        o2.setOrderId(1L);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        OrderResponseDTO o1 = new OrderResponseDTO();
        OrderResponseDTO o2 = new OrderResponseDTO();
        o1.setOrderId(1L);
        o2.setOrderId(2L);

        assertNotEquals(o1, o2);
        assertNotEquals(o1.hashCode(), o2.hashCode());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        OrderItemResponseDTO item = new OrderItemResponseDTO(
                1L, 10L, "Pizza", 2,
                new BigDecimal("250"), new BigDecimal("500")
        );

        OrderResponseDTO dto = new OrderResponseDTO(
                100L, "Dominos", new BigDecimal("500"),
                OrderStatus.PLACED, LocalDateTime.now(), List.of(item)
        );

        String result = dto.toString();
        assertNotNull(result);
        assertTrue(result.contains("Dominos"));
        assertTrue(result.contains("500"));
        assertTrue(result.contains("PLACED"));
    }
}
package com.naman.capstone.dto.response;

import com.naman.capstone.enums.Role;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * user response dto test
 */
class UserResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        UserResponseDTO dto = new UserResponseDTO(
                1L, "Naman", "Patel", "naman@gmail.com",
                "9876543210", Role.USER, new BigDecimal("1000"));

        assertEquals(1L, dto.getId());
        assertEquals("Naman", dto.getFirstName());
        assertEquals("Patel", dto.getLastName());
        assertEquals("naman@gmail.com", dto.getEmail());
        assertEquals("9876543210", dto.getPhoneNumber());
        assertEquals(Role.USER, dto.getRole());
        assertEquals(new BigDecimal("1000"), dto.getWalletBalance());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(2L);
        dto.setFirstName("Virat");
        dto.setLastName("Kohli");
        dto.setEmail("virat@gmail.com");
        dto.setPhoneNumber("9999999999");
        dto.setRole(Role.USER);
        dto.setWalletBalance(new BigDecimal("2000"));

        assertEquals(2L, dto.getId());
        assertEquals("Virat", dto.getFirstName());
        assertEquals("Kohli", dto.getLastName());
        assertEquals("virat@gmail.com", dto.getEmail());
        assertEquals("9999999999", dto.getPhoneNumber());
        assertEquals(Role.USER, dto.getRole());
        assertEquals(new BigDecimal("2000"), dto.getWalletBalance());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        UserResponseDTO u1 = new UserResponseDTO(
                1L, "Naman", "Patel", "naman@gmail.com",
                "9876543210", Role.USER, new BigDecimal("1000")
        );

        UserResponseDTO u2 = new UserResponseDTO(
                1L, "Naman", "Patel", "naman@gmail.com",
                "9876543210", Role.USER, new BigDecimal("1000")
        );

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        UserResponseDTO u1 = new UserResponseDTO(
                1L, "Naman", "Patel", "naman@gmail.com",
                "9876543210", Role.USER, new BigDecimal("1000")
        );

        UserResponseDTO u2 = new UserResponseDTO(
                2L, "Virat", "Kohli", "virat@gmail.com",
                "9999999999", Role.USER, new BigDecimal("2000")
        );

        assertNotEquals(u1, u2);
        assertNotEquals(u1.hashCode(), u2.hashCode());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        UserResponseDTO dto = new UserResponseDTO(
                1L, "Naman", "Patel", "naman@gmail.com",
                "9876543210", Role.USER, new BigDecimal("1000")
        );

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("Naman"));
        assertTrue(result.contains("Patel"));
        assertTrue(result.contains("1000"));
    }
}
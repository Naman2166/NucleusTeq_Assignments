package com.naman.capstone.entity;

import com.naman.capstone.enums.Role;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * user entity test
 */
class UserTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {
        User user = new User(
                "Naman",
                "Patel",
                "test_password",
                "naman@gmail.com",
                "9876543210",
                Role.USER,
                new BigDecimal("100")
        );

        assertEquals("Naman", user.getFirstName());
        assertEquals("Patel", user.getLastName());
        assertEquals("test_password", user.getPassword());
        assertEquals("naman@gmail.com", user.getEmail());
        assertEquals("9876543210", user.getPhoneNumber());
        assertEquals(Role.USER, user.getRole());
        assertEquals(new BigDecimal("100"), user.getWalletBalance());
    }


    /**
     * testing entity with partial data
     */
    @Test
    void test_partial_data() {
        User user = new User();

        user.setFirstName("Naman");
        user.setEmail("naman@gmail.com");

        assertEquals("Naman", user.getFirstName());
        assertEquals("naman@gmail.com", user.getEmail());
        assertNull(user.getLastName());
        assertNull(user.getPhoneNumber());
        assertEquals(BigDecimal.ZERO, user.getWalletBalance());
    }

    
    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        User user = new User();

        assertNull(user.getFirstName());
        assertNull(user.getEmail());
        assertEquals(BigDecimal.ZERO, user.getWalletBalance());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        User user = new User();

        user.setId(1L);
        user.setFirstName("naman");
        user.setLastName("patel");
        user.setPassword("test_password");
        user.setEmail("naman@gmail.com");
        user.setPhoneNumber("6260130489");
        user.setRole(Role.RESTAURANT_OWNER);
        user.setWalletBalance(new BigDecimal("500"));

        assertEquals(1L, user.getId());
        assertEquals("naman", user.getFirstName());
        assertEquals("patel", user.getLastName());
        assertEquals("test_password", user.getPassword());
        assertEquals("naman@gmail.com", user.getEmail());
        assertEquals("6260130489", user.getPhoneNumber());
        assertEquals(Role.RESTAURANT_OWNER, user.getRole());
        assertEquals(new BigDecimal("500"), user.getWalletBalance());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        User u1 = new User();
        User u2 = new User();
        u1.setId(1L);
        u2.setId(1L);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_negative() {
        User u1 = new User();
        User u2 = new User();
        u1.setId(1L);
        u2.setId(2L);

        assertNotEquals(u1, u2);
    }

    /**
     * testing toString
     */
    @Test
    void test_to_string() {
        User user = new User();
        user.setId(1L);

        assertNotNull(user.toString());
    }
}
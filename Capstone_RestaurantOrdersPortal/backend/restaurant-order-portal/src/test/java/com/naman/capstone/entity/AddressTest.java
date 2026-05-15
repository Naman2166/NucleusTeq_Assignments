package com.naman.capstone.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * address entity test
 */
class AddressTest {

    /**
     * testing entity with valid information
     */
    @Test
    void test_valid_entity() {

        User user = new User();
        user.setId(1L);
        Address address = new Address(user, "AB Road", "Indore", "MP", "452001");

        assertEquals(user, address.getUser());
        assertEquals("AB Road", address.getStreet());
        assertEquals("Indore", address.getCity());
        assertEquals("MP", address.getState());
        assertEquals("452001", address.getPincode());
    }


    /**
     * testing entity with null user
     */
    @Test
    void test_null_user() {
        Address address = new Address(null, "AB Road", "Indore", "MP", "452001");
        assertNull(address.getUser());
        assertEquals("AB Road", address.getStreet());
    }


    /**
     * testing entity with partial data
     */
    @Test
    void test_partial_data() {
        User user = new User();
        user.setId(3L);

        Address address = new Address();
        address.setUser(user);
        address.setStreet("Vijay nagar");

        assertEquals(user, address.getUser());
        assertEquals("Vijay nagar", address.getStreet());
        assertNull(address.getCity());
    }

    /**
     * testing setters and getterss
     */
    @Test
    void test_setters_and_getters() {

        Address address = new Address();
        User user = new User();
        user.setId(2L);

        address.setId(10L);
        address.setUser(user);
        address.setStreet("Vijay Nagar");
        address.setCity("Indore");
        address.setState("MP");
        address.setPincode("452010");

        assertEquals(10L, address.getId());
        assertEquals(user, address.getUser());
        assertEquals("Vijay Nagar", address.getStreet());
        assertEquals("Indore", address.getCity());
        assertEquals("MP", address.getState());
        assertEquals("452010", address.getPincode());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        User user = new User();
        user.setId(1L);

        Address a1 = new Address(user, "AB Road", "Indore", "MP", "452001");
        Address a2 = new Address(user, "AB Road", "Indore", "MP", "452001");
        a1.setId(1L);
        a2.setId(1L);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_negative() {

        Address a1 = new Address();
        Address a2 = new Address();
        a1.setId(1L);
        a2.setId(2L);

        assertNotEquals(a1, a2);
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }

    /**
     * testing toString
     */
    @Test
    void test_to_string() {
        Address address = new Address();
        address.setId(1L);
        String result = address.toString();

        assertNotNull(result);
    }
}
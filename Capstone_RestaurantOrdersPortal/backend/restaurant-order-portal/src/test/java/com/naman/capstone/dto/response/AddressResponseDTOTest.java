package com.naman.capstone.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * address response dto test
 */
class AddressResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        AddressResponseDTO dto = new AddressResponseDTO(1L, "ab road", "indore", "mp", "452001");
        assertEquals(1L, dto.getId());
        assertEquals("ab road", dto.getStreet());
        assertEquals("indore", dto.getCity());
        assertEquals("mp", dto.getState());
        assertEquals("452001", dto.getPincode());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(2L);
        dto.setStreet("vijay nagar");
        dto.setCity("indore");
        dto.setState("mp");
        dto.setPincode("452010");

        assertEquals(2L, dto.getId());
        assertEquals("vijay nagar", dto.getStreet());
        assertEquals("indore", dto.getCity());
        assertEquals("mp", dto.getState());
        assertEquals("452010", dto.getPincode());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        AddressResponseDTO a1 = new AddressResponseDTO();
        AddressResponseDTO a2 = new AddressResponseDTO();
        a1.setId(1L);
        a2.setId(1L);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        AddressResponseDTO a1 = new AddressResponseDTO();
        AddressResponseDTO a2 = new AddressResponseDTO();
        a1.setId(1L);
        a2.setId(2L);

        assertNotEquals(a1, a2);
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        AddressResponseDTO dto = new AddressResponseDTO(1L, "ab road", "indore", "mp", "452001");
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("ab road"));
    }
}
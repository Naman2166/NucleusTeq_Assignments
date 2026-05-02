package com.naman.capstone.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * address request dto test
 */
class AddressRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    /**
     * testing dto with valid information
     */
    @Test
    void test_valid_dto() {
        AddressRequestDTO requestDTO = new AddressRequestDTO("Vijay Nagar", "Indore", "MP", "452001");
        assertTrue(validator.validate(requestDTO).isEmpty());
    }

    
    /**
     * testing dto with invalid information
     */
    @Test
    void test_invalid_dto() {
        AddressRequestDTO requestDTO = new AddressRequestDTO("", "", "", "");
        assertEquals(4, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        AddressRequestDTO requestDTO = new AddressRequestDTO();
        requestDTO.setStreet("Vijay Nagar");
        requestDTO.setCity("Indore");
        requestDTO.setState("MP");
        requestDTO.setPincode("123456");

        assertEquals("Vijay Nagar", requestDTO.getStreet());
        assertEquals("Indore", requestDTO.getCity());
        assertEquals("MP", requestDTO.getState());
        assertEquals("123456", requestDTO.getPincode());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        AddressRequestDTO dto1 = new AddressRequestDTO("Vijay Nagar", "Indore", "MP", "111");
        AddressRequestDTO dto2 = new AddressRequestDTO("Vijay Nagar", "Indore", "MP", "111");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        AddressRequestDTO dto1 = new AddressRequestDTO("Vijay nagar", "Indore", "MP", "111");
        AddressRequestDTO dto2 = new AddressRequestDTO("AB road", "Indore", "MP", "222");
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }


    /**
     * testing toString
     */
    @Test
    void test_to_string() {
        AddressRequestDTO requestDTO = new AddressRequestDTO("Vijay Nagar", "Indore", "MP", "111");

        String result = requestDTO.toString();
        assertTrue(result.contains("Vijay Nagar"));
        assertTrue(result.contains("Indore"));
    }
}
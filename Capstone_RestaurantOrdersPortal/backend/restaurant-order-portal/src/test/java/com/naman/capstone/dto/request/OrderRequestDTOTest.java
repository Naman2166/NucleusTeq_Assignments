package com.naman.capstone.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * order request dto test
 */
class OrderRequestDTOTest {

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
        OrderRequestDTO requestDTO = new OrderRequestDTO(1L);
        assertTrue(validator.validate(requestDTO).isEmpty());
    }


    /**
     * null addressId should fail validation
     */
    @Test
    void test_null_addressId() {
        OrderRequestDTO requestDTO = new OrderRequestDTO(null);
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setAddressId(5L);
        assertEquals(5L, requestDTO.getAddressId());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        OrderRequestDTO requestDTO1 = new OrderRequestDTO(1L);
        OrderRequestDTO requestDTO2 = new OrderRequestDTO(1L);

        assertEquals(requestDTO1, requestDTO2);
        assertEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        OrderRequestDTO requestDTO1 = new OrderRequestDTO(1L);
        OrderRequestDTO requestDTO2 = new OrderRequestDTO(2L);

        assertNotEquals(requestDTO1, requestDTO2);
        assertNotEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * test to string
     */
    @Test
    void test_to_string() {
        OrderRequestDTO requestDTO = new OrderRequestDTO(1L);
        String result = requestDTO.toString();
        assertTrue(result.contains("1"));
    }
}
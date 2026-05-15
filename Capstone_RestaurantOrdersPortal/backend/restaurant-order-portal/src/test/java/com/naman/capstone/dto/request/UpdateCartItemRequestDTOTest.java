package com.naman.capstone.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * update cart item request dto test
 */
class UpdateCartItemRequestDTOTest {

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
        UpdateCartItemRequestDTO requestDTO = new UpdateCartItemRequestDTO(2);
        assertTrue(validator.validate(requestDTO).isEmpty());
    }


    /**
     * testing dto by inserting quantity less than one
     */
    @Test
    void test_quantity_less_than_one() {
        UpdateCartItemRequestDTO requestDTO = new UpdateCartItemRequestDTO(0);
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        UpdateCartItemRequestDTO requestDTO = new UpdateCartItemRequestDTO();
        requestDTO.setQuantity(5);
        assertEquals(5, requestDTO.getQuantity());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        UpdateCartItemRequestDTO requestDTO1 = new UpdateCartItemRequestDTO(2);
        UpdateCartItemRequestDTO requestDTO2 = new UpdateCartItemRequestDTO(2);

        assertEquals(requestDTO1, requestDTO2);
        assertEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        UpdateCartItemRequestDTO requestDTO1 = new UpdateCartItemRequestDTO(2);
        UpdateCartItemRequestDTO requestDTO2 = new UpdateCartItemRequestDTO(3);

        assertNotEquals(requestDTO1, requestDTO2);
        assertNotEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


}
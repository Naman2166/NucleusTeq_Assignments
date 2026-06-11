package com.naman.capstone.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * category request requestDTO test
 */
class CategoryRequestDTOTest {

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
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Pizza", 1L);
        assertTrue(validator.validate(requestDTO).isEmpty());
    }


    /**
     * blank name should fail validation
     */
    @Test
    void test_blank_name() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("", 1L);
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * null restaurantId should fail validation
     */
    @Test
    void test_null_restaurantId() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Pizza", null);
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Burger");
        requestDTO.setRestaurantId(2L);

        assertEquals("Burger", requestDTO.getName());
        assertEquals(2L, requestDTO.getRestaurantId());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        CategoryRequestDTO dto1 = new CategoryRequestDTO("Pizza", 1L);
        CategoryRequestDTO dto2 = new CategoryRequestDTO("Pizza", 1L);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        CategoryRequestDTO dto1 = new CategoryRequestDTO("Pizza", 1L);
        CategoryRequestDTO dto2 = new CategoryRequestDTO("Burger", 2L);

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

}
package com.naman.capstone.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * cart request dto test
 */
class CartRequestDTOTest {

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
        CartRequestDTO dto = new CartRequestDTO(1L, 2);
        assertTrue(validator.validate(dto).isEmpty());
    }


    /**
     * null menuItemId should fail validation
     */
    @Test
    void test_null_menuItemId() {
        CartRequestDTO dto = new CartRequestDTO(null, 2);
        assertEquals(1, validator.validate(dto).size());
    }


    /**
     * test quantity less than one should fail validation
     */
    @Test
    void test_quantity_less_than_one() {
        CartRequestDTO dto = new CartRequestDTO(1L, 0);

        assertEquals(1, validator.validate(dto).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        CartRequestDTO dto = new CartRequestDTO();
        dto.setMenuItemId(10L);
        dto.setQuantity(5);

        assertEquals(10L, dto.getMenuItemId());
        assertEquals(5, dto.getQuantity());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        CartRequestDTO dto1 = new CartRequestDTO(1L, 2);
        CartRequestDTO dto2 = new CartRequestDTO(1L, 2);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        CartRequestDTO dto1 = new CartRequestDTO(1L, 2);
        CartRequestDTO dto2 = new CartRequestDTO(2L, 3);

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }


    /**
     * test to string
     */
    @Test
    void test_to_string() {
        CartRequestDTO dto = new CartRequestDTO(1L, 2);

        String result = dto.toString();
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
    }
}
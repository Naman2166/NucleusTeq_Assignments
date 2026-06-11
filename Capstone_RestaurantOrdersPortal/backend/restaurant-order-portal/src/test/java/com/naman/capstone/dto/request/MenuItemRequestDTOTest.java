package com.naman.capstone.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * menu item request dto test
 */
class MenuItemRequestDTOTest {

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
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO("Pizza", new BigDecimal("200.12"), 1L, 1L, "image.jpg");
        assertTrue(validator.validate(requestDTO).isEmpty());
    }


    /**
     * blank name should fail validation
     */
    @Test
    void test_blank_name() {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO("", new BigDecimal("199.99"), 1L, 1L, "image.jpg");
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * null price should fail validation
     */
    @Test
    void test_null_price() {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO("Pizza", null, 1L, 1L, "image.jpg");
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * null categoryId should fail validation
     */
    @Test
    void test_null_categoryId() {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO("Pizza", new BigDecimal("150.123"), null, 1L, "image.jpg");
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * null restaurantId should fail validation
     */
    @Test
    void test_null_restaurantId() {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO("Pizza", new BigDecimal("199.99"), 1L, null, "image.jpg");
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        MenuItemRequestDTO requestDTO = new MenuItemRequestDTO();
        requestDTO.setName("Burger");
        requestDTO.setPrice(new BigDecimal("99.99"));
        requestDTO.setCategoryId(2L);
        requestDTO.setRestaurantId(3L);
        requestDTO.setImageUrl("img.png");

        assertEquals("Burger", requestDTO.getName());
        assertEquals(new BigDecimal("99.99"), requestDTO.getPrice());
        assertEquals(2L, requestDTO.getCategoryId());
        assertEquals(3L, requestDTO.getRestaurantId());
        assertEquals("img.png", requestDTO.getImageUrl());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        MenuItemRequestDTO requestDTO1 = new MenuItemRequestDTO("Pizza", new BigDecimal("199.99"), 1L, 1L, "img.jpg");
        MenuItemRequestDTO requestDTO2 = new MenuItemRequestDTO("Pizza", new BigDecimal("199.99"), 1L, 1L, "img.jpg");

        assertEquals(requestDTO1, requestDTO2);
        assertEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        MenuItemRequestDTO requestDTO1 = new MenuItemRequestDTO("Pizza", new BigDecimal("200"), 1L, 1L, "img.jpg");
        MenuItemRequestDTO requestDTO2 = new MenuItemRequestDTO("Burger", new BigDecimal("99.99"), 2L, 2L, "img2.jpg");

        assertNotEquals(requestDTO1, requestDTO2);
        assertNotEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


}
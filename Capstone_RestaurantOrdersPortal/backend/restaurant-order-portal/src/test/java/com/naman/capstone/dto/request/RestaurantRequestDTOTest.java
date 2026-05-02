package com.naman.capstone.dto.request;

import com.naman.capstone.enums.RestaurantStatus;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * restaurant request dto test
 */
class RestaurantRequestDTOTest {

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
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO(
                "Dominos",
                "Indore",
                RestaurantStatus.OPEN,
                "img.jpg"
        );
        assertTrue(validator.validate(requestDTO).isEmpty());
    }


    /**
     * blank name should fail validation
     */
    @Test
    void test_blank_name() {
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO(
                "",
                "Indore",
                RestaurantStatus.OPEN,
                "img.jpg"
        );
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * blank address should fail validation
     */
    @Test
    void test_blank_address() {
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO(
                "Dominos",
                "",
                RestaurantStatus.OPEN,
                "img.jpg"
        );
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * null status should fail validation
     */
    @Test
    void test_null_status() {
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO(
                "Dominos",
                "Indore",
                null,
                "img.jpg"
        );
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO();
        requestDTO.setName("Dominos");
        requestDTO.setAddress("Bhopal");
        requestDTO.setStatus(RestaurantStatus.CLOSED);
        requestDTO.setImageUrl("img2.jpg");

        assertEquals("Dominos", requestDTO.getName());
        assertEquals("Bhopal", requestDTO.getAddress());
        assertEquals(RestaurantStatus.CLOSED, requestDTO.getStatus());
        assertEquals("img2.jpg", requestDTO.getImageUrl());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        RestaurantRequestDTO requestDTO1 = new RestaurantRequestDTO(
                "Dominos",
                "Indore",
                RestaurantStatus.OPEN,
                "img.jpg"
        );

        RestaurantRequestDTO requestDTO2 = new RestaurantRequestDTO(
                "Dominos",
                "Indore",
                RestaurantStatus.OPEN,
                "img.jpg"
        );

        assertEquals(requestDTO1, requestDTO2);
        assertEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }

    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        RestaurantRequestDTO requestDTO1 = new RestaurantRequestDTO(
                "Dominos",
                "Indore",
                RestaurantStatus.OPEN,
                "img.jpg"
        );

        RestaurantRequestDTO requestDTO2 = new RestaurantRequestDTO(
                "Pizza Hut",
                "Bhopal",
                RestaurantStatus.CLOSED,
                "img2.jpg"
        );

        assertNotEquals(requestDTO1, requestDTO2);
        assertNotEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * test to string
     */
    @Test
    void test_to_string() {
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO(
                "Dominos",
                "Indore",
                RestaurantStatus.OPEN,
                "img.jpg"
        );
        String result = requestDTO.toString();

        assertTrue(result.contains("Dominos"));
        assertTrue(result.contains("Indore"));
    }
}
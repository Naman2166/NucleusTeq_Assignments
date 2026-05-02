package com.naman.capstone.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * login request dto test
 */
class LoginRequestDTOTest {

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
        LoginRequestDTO requestDTO = new LoginRequestDTO("naman@gmail.com", "test_password");
        assertTrue(validator.validate(requestDTO).isEmpty());
    }


    /**
     * blank email should fail validation
     */
    @Test
    void test_blank_email() {
        LoginRequestDTO requestDTO = new LoginRequestDTO(" ", "test_password");
        assertEquals(2, validator.validate(requestDTO).size());
    }


    /**
     * invalid email format should fail validation
     */
    @Test
    void test_invalid_email_format() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("naman-email", "test_password");
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * blank password should fail validation
     */
    @Test
    void test_blank_password() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("naman@gmail.com", "");
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("naman@gmail.com");
        requestDTO.setPassword("test_password");

        assertEquals("naman@gmail.com", requestDTO.getEmail());
        assertEquals("test_password", requestDTO.getPassword());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        LoginRequestDTO requestDTO1 = new LoginRequestDTO("naman@gmail.com", "test_password");
        LoginRequestDTO requestDTO2 = new LoginRequestDTO("naman@gmail.com", "test_password");
        assertEquals(requestDTO1, requestDTO2);
        assertEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        LoginRequestDTO requestDTO1 = new LoginRequestDTO("naman@gmail.com", "test_password");
        LoginRequestDTO requestDTO2 = new LoginRequestDTO("patel@gmail.com", "test");

        assertNotEquals(requestDTO1, requestDTO2);
        assertNotEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * test to string
     */
    @Test
    void test_to_string() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("naman@gmail.com", "test_password");
        String result = requestDTO.toString();

        assertTrue(result.contains("naman@gmail.com"));
        assertTrue(result.contains("[PROTECTED]"));
    }
}
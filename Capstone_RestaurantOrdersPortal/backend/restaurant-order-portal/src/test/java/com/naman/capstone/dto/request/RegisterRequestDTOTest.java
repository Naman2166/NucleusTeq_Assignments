package com.naman.capstone.dto.request;

import com.naman.capstone.enums.Role;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * register request dto test
 */
class RegisterRequestDTOTest {

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
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "naman@gmail.com",
                "test_password",
                "9876543210",
                Role.USER
        );
        assertTrue(validator.validate(requestDTO).isEmpty());
    }


    /**
     * blank first name should fail validation
     */
    @Test
    void test_blank_firstName() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "",
                "Patel",
                "naman@gmail.com",
                "test_password",
                "9876543210",
                Role.USER
        );

        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * blank last name should fail validation
     */
    @Test
    void test_blank_lastName() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "Naman",
                " ",
                "naman@gmail.com",
                "test_password",
                "9876543210",
                Role.USER
        );

        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * invalid email should fail validation
     */
    @Test
    void test_invalid_email() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "galatHainBhai",
                "test_password",
                "9876543210",
                Role.USER
        );
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * blank password should fail validation
     */
    @Test
    void test_blank_password() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "naman@gmail.com",
                "",
                "9876543210",
                Role.USER
        );
        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * invalid phone number should fail validation
     */
    @Test
    void test_invalid_phoneNumber() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "naman@gmail.com",
                "test_password",
                "12345",
                Role.USER
        );

        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * null role should fail validation
     */
    @Test
    void test_null_role() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "test@gmail.com",
                "test_password",
                "9876543210",
                null
        );

        assertEquals(1, validator.validate(requestDTO).size());
    }


    /**
     * testing getters and setters
     */
    @Test
    void test_getters_and_setters() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();

        requestDTO.setFirstName("A");
        requestDTO.setLastName("B");
        requestDTO.setEmail("a@gmail.com");
        requestDTO.setPassword("test_password");
        requestDTO.setPhoneNumber("9999999999");
        requestDTO.setRole(Role.USER);

        assertEquals("A", requestDTO.getFirstName());
        assertEquals("B", requestDTO.getLastName());
        assertEquals("a@gmail.com", requestDTO.getEmail());
        assertEquals("test_password", requestDTO.getPassword());
        assertEquals("9999999999", requestDTO.getPhoneNumber());
        assertEquals(Role.USER, requestDTO.getRole());
    }


    /**
     * testing equals and hashcode for identical object
     */
    @Test
    void test_equals_and_hashcode() {
        RegisterRequestDTO requestDTO1 = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "test@gmail.com",
                "test_password",
                "9876543210",
                Role.USER
        );

        RegisterRequestDTO requestDTO2 = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "test@gmail.com",
                "test_password",
                "9876543210",
                Role.USER
        );

        assertEquals(requestDTO1, requestDTO2);
        assertEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_not_equals_and_hashcode() {
        RegisterRequestDTO requestDTO1 = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "test@gmail.com",
                "test_password",
                "9876543210",
                Role.USER
        );

        RegisterRequestDTO requestDTO2 = new RegisterRequestDTO(
                "A",
                "B",
                "a@gmail.com",
                "test",
                "9999999999",
                Role.USER
        );

        assertNotEquals(requestDTO1, requestDTO2);
        assertNotEquals(requestDTO1.hashCode(), requestDTO2.hashCode());
    }


    /**
     * test to string
     */
    @Test
    void test_to_string() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(
                "Naman",
                "Patel",
                "naman@gmail.com",
                "test_password",
                "9876543210",
                Role.USER
        );

        String result = requestDTO.toString();

        assertTrue(result.contains("Naman"));
        assertTrue(result.contains("Patel"));
        assertTrue(result.contains("naman@gmail.com"));
        assertTrue(result.contains("[PROTECTED]"));
    }
}
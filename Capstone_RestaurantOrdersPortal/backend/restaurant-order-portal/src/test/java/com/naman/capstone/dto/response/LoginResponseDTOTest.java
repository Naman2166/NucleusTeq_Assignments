package com.naman.capstone.dto.response;

import com.naman.capstone.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * login response dto test
 */
class LoginResponseDTOTest {

    /**
     * testing constructor
     */
    @Test
    void test_constructor() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        LoginResponseDTO dto = new LoginResponseDTO("token123", user, Role.USER);

        assertEquals("token123", dto.getToken());
        assertEquals(user, dto.getUser());
        assertEquals(Role.USER, dto.getRole());
    }


    /**
     * testing setters and getters
     */
    @Test
    void test_setters_and_getters() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(2L);

        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setToken("token456");
        dto.setUser(user);
        dto.setRole(Role.USER);

        assertEquals("token456", dto.getToken());
        assertEquals(user, dto.getUser());
        assertEquals(Role.USER, dto.getRole());
    }


    /**
     * testing equals and hashcode for same object
     */
    @Test
    void test_equals_and_hashcode_same() {
        UserResponseDTO user1 = new UserResponseDTO();
        user1.setId(1L);
        UserResponseDTO user2 = new UserResponseDTO();
        user2.setId(1L);

        LoginResponseDTO l1 = new LoginResponseDTO("token123", user1, Role.USER);
        LoginResponseDTO l2 = new LoginResponseDTO("token123", user2, Role.USER);

        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }


    /**
     * testing equals and hashcode for different object
     */
    @Test
    void test_equals_and_hashcode_different() {
        UserResponseDTO user1 = new UserResponseDTO();
        user1.setId(1L);
        UserResponseDTO user2 = new UserResponseDTO();
        user2.setId(2L);

        LoginResponseDTO l1 = new LoginResponseDTO("token123", user1, Role.USER);
        LoginResponseDTO l2 = new LoginResponseDTO("token456", user2, Role.RESTAURANT_OWNER);

        assertNotEquals(l1, l2);
        assertNotEquals(l1.hashCode(), l2.hashCode());
    }


    /**
     * testing tostring
     */
    @Test
    void test_to_string() {
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        LoginResponseDTO dto = new LoginResponseDTO("token123", user, Role.USER);
        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("[PROTECTED]"));
        assertTrue(result.contains("USER"));
    }
}
package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.LoginRequestDTO;
import com.naman.capstone.dto.request.RegisterRequestDTO;
import com.naman.capstone.dto.response.LoginResponseDTO;
import com.naman.capstone.dto.response.UserResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.enums.Role;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * user controller tests
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;
    

    /**
     * creates mock user
     */
    private User mockUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("naman@gmail.com");
        return user;
    }

    /**
     * creates mock register request
     */
    private RegisterRequestDTO mockRegisterRequest() {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setEmail("naman@gmail.com");
        requestDTO.setPassword("test_password");
        requestDTO.setFirstName("naman");
        requestDTO.setLastName("patel");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setRole(Role.USER);
        return requestDTO;
    }

    /**
     * creates mock login request
     */
    private LoginRequestDTO mockLoginRequest() {
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("naman@gmail.com");
        requestDTO.setPassword("test_password");
        return requestDTO;
    }


    /**
     * creates mock user response
     */
    private UserResponseDTO mockUserResponse() {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEmail("naman@gmail.com");
        responseDTO.setFirstName("naman");
        responseDTO.setLastName("patel");
        responseDTO.setPhoneNumber("1234567890");
        responseDTO.setRole(Role.USER);
        return responseDTO;
    }

    /**
     * creates mock login response
     */
    private LoginResponseDTO mockLoginResponse() {
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken("dummy_token");
        return responseDTO;
    }



    /**
     * testing register user success
     */
    @Test
    void register_user_success() throws Exception {

        when(userService.register(any())).thenReturn(mockUserResponse());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRegisterRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing login user success
     */
    @Test
    void login_user_success() throws Exception {

        when(userService.login(any())).thenReturn(mockLoginResponse());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockLoginRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy_token"));
    }


    /**
     * testing get current user success
     */
    @Test
    @WithMockUser
    void get_current_user_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(mockUser());

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing register user validation failure
     */
    @Test
    void register_user_validation_failure() throws Exception {

        RegisterRequestDTO requestDTO = new RegisterRequestDTO();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing register user validation failure with partial data
     */
    @Test
    void register_user_partial_data_failure() throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setEmail("test@gmail.com");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing login user validation failure
     */
    @Test
    void login_user_validation_failure() throws Exception {

        LoginRequestDTO requestDTO = new LoginRequestDTO();

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing login user failure when service throws exception
     */
    @Test
    void login_user_failure() throws Exception {

        when(userService.login(any())).thenThrow(new RuntimeException("invalid credentials"));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockLoginRequest())))
                .andExpect(status().isInternalServerError());
    }


    /**
     * testing get current user failure when service returns null
     */
    @Test
    @WithMockUser
    void get_current_user_failure() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class))).thenReturn(null);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isInternalServerError());
    }
}
package com.naman.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naman.capstone.dto.request.AddressRequestDTO;
import com.naman.capstone.dto.response.AddressResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.security.JwtFilter;
import com.naman.capstone.service.AddressService;
import com.naman.capstone.service.CurrentUserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * address controller tests
 */
@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     *  mock user
     */
    private User mockUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    /**
     * mock request dto
     */
    private AddressRequestDTO mockRequest() {
        AddressRequestDTO requestDTO = new AddressRequestDTO();
        requestDTO.setCity("Indore");
        requestDTO.setStreet("Geeta Bhawan");
        requestDTO.setPincode("411001");
        requestDTO.setState("MP");
        return requestDTO;
    }

    /**
     *  mock response dto
     */
    private AddressResponseDTO mockResponse() {
        AddressResponseDTO responseDTO = new AddressResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCity("Indore");
        responseDTO.setStreet("Geeta Bhawan");
        responseDTO.setPincode("411001");
        responseDTO.setState("MP");
        return responseDTO;
    }



    /**
     * testing add address success
     */
    @Test
    @WithMockUser
    void add_address_success() throws Exception {

        when(currentUserService.getCurrentUser(any()))
                .thenReturn(mockUser());
        when(addressService.addAddress(any(User.class), any(AddressRequestDTO.class)))
                .thenReturn(mockResponse());

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing add address validation failure
     */
    @Test
    @WithMockUser
    void add_address_validation_failure() throws Exception {

        AddressRequestDTO request = new AddressRequestDTO();

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing get all addresses success
     */
    @Test
    @WithMockUser
    void get_addresses_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(addressService.getUserAddresses(any(User.class)))
                .thenReturn(List.of(mockResponse()));

        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }


    /**
     * testing get addresses when empty
     */
    @Test
    @WithMockUser
    void get_addresses_empty() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(addressService.getUserAddresses(any(User.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }


    /**
     * testing update address success
     */
    @Test
    @WithMockUser
    void update_address_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        when(addressService.updateAddress(any(User.class), eq(1L), any(AddressRequestDTO.class)))
                .thenReturn(mockResponse());

        mockMvc.perform(put("/api/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }


    /**
     * testing update address validation failure
     */
    @Test
    @WithMockUser
    void update_address_validation_failure() throws Exception {

        AddressRequestDTO request = new AddressRequestDTO();

        mockMvc.perform(put("/api/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    /**
     * testing delete address success
     */
    @Test
    @WithMockUser
    void delete_address_success() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        doNothing().when(addressService)
                .deleteAddress(any(User.class), eq(1L));

        mockMvc.perform(delete("/api/addresses/1"))
                .andExpect(status().isNoContent());
    }


    /**
     * testing delete address failure
     */
    @Test
    @WithMockUser
    void delete_address_failure() throws Exception {

        when(currentUserService.getCurrentUser(any(UserDetails.class)))
                .thenReturn(mockUser());
        doThrow(new RuntimeException("error")).when(addressService)
                .deleteAddress(any(User.class), eq(1L));

        mockMvc.perform(delete("/api/addresses/1"))
                .andExpect(status().isInternalServerError());
    }


}
package com.naman.capstone.service;

import com.naman.capstone.dto.request.AddressRequestDTO;
import com.naman.capstone.dto.response.AddressResponseDTO;
import com.naman.capstone.entity.Address;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.repository.AddressRepository;
import com.naman.capstone.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AddressService.
 */
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    AddressServiceImpl addressService;


    /**
     * Test adding address
     */
    @Test
    void add_address_success() {

        User user = new User();
        user.setId(1L);

        AddressRequestDTO request = new AddressRequestDTO();
        request.setStreet("Street");
        request.setCity("Bhopal");
        request.setState("MP");
        request.setPincode("462001");

        Address saved = new Address();
        saved.setId(1L);
        saved.setUser(user);

        when(addressRepository.save(any(Address.class))).thenReturn(saved);
        AddressResponseDTO res = addressService.addAddress(user, request);

        assertNotNull(res);
    }


    /**
     * Test get user addresses
     */
    @Test
    void get_user_addresses_success() {
        User user = new User();
        user.setId(1L);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        when(addressRepository.findByUser(user)).thenReturn(List.of(address));
        List<AddressResponseDTO> list = addressService.getUserAddresses(user);

        assertEquals(1, list.size());
    }


    /**
     * Test update address success
     */
    @Test
    void update_address_success() {
        User user = new User();
        user.setId(1L);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        AddressRequestDTO request = new AddressRequestDTO();
        request.setStreet("Updated");

        when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(address));
        AddressResponseDTO res = addressService.updateAddress(user, 1L, request);

        assertNotNull(res);
    }


    /**
     * Test update address not found
     */
    @Test
    void update_address_not_found() {
        User user = new User();
        user.setId(1L);

        when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> addressService.updateAddress(user, 1L, new AddressRequestDTO()));
    }


    /**
     * Test delete address success
     */
    @Test
    void delete_address_success() {
        User user = new User();
        user.setId(1L);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(address));
        addressService.deleteAddress(user, 1L);

        verify(addressRepository).delete(address);
    }


    /**
     * Test delete address not found
     */
    @Test
    void delete_address_not_found() {
        User user = new User();
        user.setId(1L);

        when(addressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress(user, 1L));
    }
}
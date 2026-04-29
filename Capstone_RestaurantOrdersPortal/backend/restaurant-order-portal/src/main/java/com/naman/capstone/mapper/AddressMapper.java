package com.naman.capstone.mapper;

import com.naman.capstone.dto.response.AddressResponseDTO;
import com.naman.capstone.entity.Address;

/**
 * Maps Address entity to DTO
 */
public class AddressMapper {

    public static AddressResponseDTO toDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPincode()
        );
    }
}
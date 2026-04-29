package com.naman.capstone.service;

import com.naman.capstone.dto.request.AddressRequestDTO;
import com.naman.capstone.dto.response.AddressResponseDTO;
import com.naman.capstone.entity.User;

import java.util.List;

/**
 * Service interface for managing user addresses
 */
public interface AddressService {

    /**
     * Add new address for a user
     * @param user user who will own this address
     * @param request address details
     * @return saved address data
     */
    AddressResponseDTO addAddress(User user, AddressRequestDTO request);

    /**
     * Retrieves all addresses of a user
     * @param user user whose address need to fetch
     * @return list of user addresses
     */
    List<AddressResponseDTO> getUserAddresses(User user);

    /**
     * Updates an existing address
     * @param user user whose address need to update
     * @param addressId address id
     * @param request updated address details
     * @return updated address data
     */
    AddressResponseDTO updateAddress(User user, Long addressId, AddressRequestDTO request);

    /**
     * deletes an address
     * @param user user whose address need to delete
     * @param addressId address id
     */
    void deleteAddress(User user, Long addressId);
}
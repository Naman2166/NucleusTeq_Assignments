package com.naman.capstone.service.impl;

import com.naman.capstone.dto.request.AddressRequestDTO;
import com.naman.capstone.dto.response.AddressResponseDTO;
import com.naman.capstone.entity.Address;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.mapper.AddressMapper;
import com.naman.capstone.repository.AddressRepository;
import com.naman.capstone.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * service implementation for managing user addresses
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);


    /**
     * add new address for user
     * @param user logged-in user
     * @param request address data
     * @return saved address
     */
    @Override
    public AddressResponseDTO addAddress(User user, AddressRequestDTO request) {

        logger.info("adding address for user id: {}", user.getId());
        Address address = new Address(
                user,
                request.getStreet(),
                request.getCity(),
                request.getState(),
                request.getPincode()
        );

        Address saved = addressRepository.save(address);
        logger.info("address added with id: {}", saved.getId());

        return AddressMapper.toDTO(saved);
    }


    /**
     * get all addresses of user
     * @param user logged-in user
     * @return list of addresses
     */
    @Override
    public List<AddressResponseDTO> getUserAddresses(User user) {

        logger.info("fetching addresses for user id: {}", user.getId());
        List<Address> addresses = addressRepository.findByUser(user);
        List<AddressResponseDTO> result = new ArrayList<>();

        for (Address address : addresses) {
            result.add(AddressMapper.toDTO(address));
        }

        return result;
    }


    /**
     * update address
     * @param user logged-in user
     * @param addressId id of address
     * @param request new address data
     * @return updated address
     */
    @Override
    public AddressResponseDTO updateAddress(User user, Long addressId, AddressRequestDTO request) {

        logger.info("updating address id {} for user id {}", addressId, user.getId());
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> {
                        logger.error("address not found for id {}", addressId);
                        return new ResourceNotFoundException("Address not found");
                });

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        logger.info("address updated successfully for id {}", addressId);

        return AddressMapper.toDTO(address);
    }


    /**
     * delete address
     * @param user logged-in user
     * @param addressId id of address
     */
    @Override
    public void deleteAddress(User user, Long addressId) {

        logger.info("deleting address id {} for user id {}", addressId, user.getId());
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> {
                    logger.error("address not found for id {}", addressId);
                    return new ResourceNotFoundException("Address not found");
                });

        addressRepository.delete(address);
        logger.info("address deleted successfully for id {}", addressId);
    }
}
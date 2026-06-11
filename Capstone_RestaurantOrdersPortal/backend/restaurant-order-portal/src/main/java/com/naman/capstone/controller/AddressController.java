package com.naman.capstone.controller;

import com.naman.capstone.dto.request.AddressRequestDTO;
import com.naman.capstone.dto.response.AddressResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.service.AddressService;
import com.naman.capstone.service.CurrentUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naman.capstone.constant.AppConstants.ADDRESS_BASE_URL;
import static com.naman.capstone.constant.AppConstants.ADDRESS_ID;

/**
 * REST controller for managing user addresses
 */
@RestController
@RequestMapping(ADDRESS_BASE_URL)
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;
    private final CurrentUserService currentUserService;

    public AddressController(AddressService addressService, CurrentUserService currentUserService) {
        this.addressService = addressService;
        this.currentUserService = currentUserService;
    }



    /**
     * Add new address for authenticated user
     * @param requestDTO address details of user
     * @param userDetails authenticated user details
     * @return created address
     */
    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress(
            @Valid @RequestBody AddressRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Request received to add new address");
        User user = currentUserService.getCurrentUser(userDetails);

        AddressResponseDTO response = addressService.addAddress(user, requestDTO);
        log.info("Address created successfully for userId={}", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Retrieves all addresses of user
     * @param userDetails authenticated user details
     * @return list of addresses of user
     */
    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(@AuthenticationPrincipal UserDetails userDetails) {

        log.info("Fetching all addresses of a user");
        User user = currentUserService.getCurrentUser(userDetails);

        List<AddressResponseDTO> response = addressService.getUserAddresses(user);
        log.info("Fetched all addresses for userId={}", user.getId());
        return ResponseEntity.ok(response);
    }


    /**
     * Update an existing address
     * @param addressId id of the address to update
     * @param requestDTO updated address details
     * @param userDetails authenticated user details
     * @return updated address
     */
    @PutMapping(ADDRESS_ID)
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Request received to update addressId={}", addressId);
        User user = currentUserService.getCurrentUser(userDetails);

        AddressResponseDTO response = addressService.updateAddress(user, addressId, requestDTO);
        log.info("Address added successfully for userId={}", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Delete an address of a user
     * @param addressId id of the address to delete
     * @param userDetails authenticated user details
     * @return no content response
     */
    @DeleteMapping(ADDRESS_ID)
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = currentUserService.getCurrentUser(userDetails);

        log.warn("Request received to delete addressId={}", addressId);
        addressService.deleteAddress(user, addressId);

        log.info("Address deleted successfully for addressId={}", addressId);
        return ResponseEntity.noContent().build();
    }
}
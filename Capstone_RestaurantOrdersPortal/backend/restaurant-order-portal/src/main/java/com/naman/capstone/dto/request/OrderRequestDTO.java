package com.naman.capstone.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * DTO for placing an order
 */
public class OrderRequestDTO {

    /**
     * id of the address where the order will be delivered
     */
    @NotNull(message = "Address id is required")
    private Long addressId;


    /**
     * Default constructor
     */
    public OrderRequestDTO() {}

    /**
     * Parameterized constructor
     */
    public OrderRequestDTO(Long addressId) {
        this.addressId = addressId;
    }


    /**
     * getter setter
     */
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }


    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "OrderRequestDTO{" + "addressId=" + addressId + '}';
    }

    /**
     * Compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderRequestDTO)) return false;
        OrderRequestDTO other = (OrderRequestDTO) obj;
        return Objects.equals(addressId, other.addressId);
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(addressId);
    }
}
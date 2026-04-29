package com.naman.capstone.dto.request;

import jakarta.validation.constraints.Min;

import java.util.Objects;

/**
 * DTO for updating quantity of an item in the cart
 */
public class UpdateCartItemRequestDTO {

    /**
     * Quantity of the cart item
     * must be at least 1
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;


    /**
     * Default constructor
     */
    public UpdateCartItemRequestDTO() {
    }

    /**
     * Parameterized constructor
     */
    public UpdateCartItemRequestDTO(int quantity) {
        this.quantity = quantity;
    }



    /**
     * getter setter
     */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "UpdateCartItemRequestDTO{" + "quantity=" + quantity + "}";
    }

    /**
     * Compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UpdateCartItemRequestDTO)) return false;
        UpdateCartItemRequestDTO otherObj = (UpdateCartItemRequestDTO) obj;
        return quantity == otherObj.quantity;
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
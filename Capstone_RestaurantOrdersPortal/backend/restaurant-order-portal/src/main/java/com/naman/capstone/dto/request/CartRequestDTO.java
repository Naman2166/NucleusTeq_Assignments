package com.naman.capstone.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Request DTO for adding an item to the cart
 */
public class CartRequestDTO {

    /**
     * id of the menu item to be added
     */
    @NotNull(message = "Menu item id is required")
    private Long menuItemId;

    /**
     * Quantity of the item
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;


    /**
     * Default constructor
     */
    public CartRequestDTO() {}

    /**
     * Parameterized constructor
     */
    public CartRequestDTO(Long menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }


    /**
     * Getters setters methods
     */
    public Long getMenuItemId() { return menuItemId; }

    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }


    /**
     * Returns string representation of this object
     */
    @Override
    public String toString() {
        return "CartRequestDTO{" + "menuItemId=" + menuItemId + ", quantity=" + quantity + "}";
    }

    /**
     * Compares this object with another for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CartRequestDTO)) return false;
        CartRequestDTO otherObj = (CartRequestDTO) obj;
        return quantity == otherObj.quantity && Objects.equals(menuItemId, otherObj.menuItemId);
    }

    /**
     * it generates hash code for this DTO object
     */
    @Override
    public int hashCode() {
        return Objects.hash(menuItemId, quantity);
    }
}
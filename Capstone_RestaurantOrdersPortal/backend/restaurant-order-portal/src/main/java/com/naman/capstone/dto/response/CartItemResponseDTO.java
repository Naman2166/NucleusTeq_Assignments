package com.naman.capstone.dto.response;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * response DTO representing a single item in the cart
 */
public class CartItemResponseDTO {

    /**
     * id of the cart item
     */
    private Long id;

    /**
     * id of the menu item
     */
    private Long menuItemId;

    /**
     * Name of the menu item
     */
    private String menuItemName;

    /**
     * Quantity of the item in cart
     */
    private int quantity;

    /**
     * total price for this item
     */
    private BigDecimal totalPrice;


    /**
     * Default constructor
     */
    public CartItemResponseDTO() {}

    /**
     * Parameterized constructor
     */
    public CartItemResponseDTO(Long id, Long menuItemId, String menuItemName, int quantity, BigDecimal totalPrice) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


    /**
     * Getters and setters for all fields
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "CartItemResponseDTO{" + "id=" + id +
                ", menuItemId=" + menuItemId +
                ", menuItemName='" + menuItemName +
                "', quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                "}";
    }

    /**
     * Compares objects based on id for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CartItemResponseDTO)) return false;
        CartItemResponseDTO otherObj = (CartItemResponseDTO) obj;
        return Objects.equals(id, otherObj.id);
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
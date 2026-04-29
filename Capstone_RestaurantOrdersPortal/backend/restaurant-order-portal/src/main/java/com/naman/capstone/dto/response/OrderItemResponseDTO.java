package com.naman.capstone.dto.response;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * dto representing order item response data
 */
public class OrderItemResponseDTO {

    /**
     * id of order item
     */
    private Long id;

    /**
     * id of menu item
     */
    private Long menuItemId;

    /**
     * name of menu item
     */
    private String menuItemName;

    /**
     * quantity of item
     */
    private int quantity;

    /**
     * price of a single item
     */
    private BigDecimal unitPrice;

    /**
     * total price of item
     * here totalPrice = unitPrice x quantity
     */
    private BigDecimal totalPrice;



    /**
     * default constructor
     */
    public OrderItemResponseDTO() {}

    /**
     * parameterized constructor
     */
    public OrderItemResponseDTO(Long id, Long menuItemId, String menuItemName,
                                int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }


    /**
     * getters and setters for all fields
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "OrderItemResponseDTO{" + "id=" + id +
                ", menuItemId=" + menuItemId +
                ", menuItemName='" + menuItemName +
                "', quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                "}";
    }

    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderItemResponseDTO)) return false;
        OrderItemResponseDTO other = (OrderItemResponseDTO) obj;
        return Objects.equals(id, other.id);
    }

    /**
     * generates hash code based on id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
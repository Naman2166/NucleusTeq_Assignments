package com.naman.capstone.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * DTO representing cart response data
 */
public class CartResponseDTO {

    /**
     * cart id
     */
    private Long cartId;

    /**
     * total price of all items in cart
     */
    private BigDecimal totalPrice;

    /**
     * list of all items in cart
     */
    private List<CartItemResponseDTO> items;


    /**
     * Default constructor
     */
    public CartResponseDTO() {}

    /**
     * Parameterized constructor
     */
    public CartResponseDTO(Long cartId, BigDecimal totalPrice, List<CartItemResponseDTO> items) {
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.items = items;
    }


    /**
     * Getters and setters for all fields
     */
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponseDTO> items) {
        this.items = items;
    }


    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "CartResponseDTO{" +
                "cartId=" + cartId +
                ", totalPrice=" + totalPrice +
                ", items=" + items +
                "}";
    }

    /**
     * Compares this object with another for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CartResponseDTO)) return false;
        CartResponseDTO otherObj = (CartResponseDTO) obj;
        return Objects.equals(cartId, otherObj.cartId);
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(cartId);
    }
}
package com.naman.capstone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Represents the data received from owner for creating or updating a menu items
 */
public class MenuItemRequestDTO {

    /**
     * name of the menu item
     */
    @NotBlank(message = "Name cannot be empty")
    private String name;

    /**
     * price of the menu item
     */
    @NotNull(message = "Price is required")
    private BigDecimal price;

    /**
     * id of the category this item belongs to
     */
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    /**
     * id of the restaurant this item belongs to
     */
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;


    /**
     * Default constructor
     */
    public MenuItemRequestDTO() {}

    /**
     * Parameterized constructor
     */
    public MenuItemRequestDTO(String name, BigDecimal price, Long categoryId, Long restaurantId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.restaurantId = restaurantId;
    }


    /**
     * getters setter
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }


    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "MenuItemRequestDTO{" +
                "name='" + name +
                "', price=" + price +
                ", categoryId=" + categoryId +
                ", restaurantId=" + restaurantId +
                "}";
    }

    /**
     * Compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MenuItemRequestDTO)) return false;
        MenuItemRequestDTO otherObj = (MenuItemRequestDTO) obj;
        return java.util.Objects.equals(name, otherObj.name) &&
                java.util.Objects.equals(price, otherObj.price) &&
                java.util.Objects.equals(categoryId, otherObj.categoryId) &&
                java.util.Objects.equals(restaurantId, otherObj.restaurantId);
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, price, categoryId, restaurantId);
    }
    
}
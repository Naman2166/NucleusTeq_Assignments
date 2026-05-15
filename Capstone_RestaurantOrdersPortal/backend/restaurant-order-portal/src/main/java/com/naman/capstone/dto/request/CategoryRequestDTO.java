package com.naman.capstone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Represents the data received from owner for creating or updating a category
 */
public class CategoryRequestDTO {

    /**
     * Name of the category
     */
    @NotBlank(message = "Name cannot be empty")
    private String name;

    /**
     * id of the restaurant to which this category belongs
     */
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;


    /**
     * Default constructor
     */
    public CategoryRequestDTO() {}

    /**
     * Parameterized constructor
     */
    public CategoryRequestDTO(String name, Long restaurantId) {
        this.name = name;
        this.restaurantId = restaurantId;
    }


    /**
     * getters setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "CategoryRequestDTO{" + "name='" + name + "', restaurantId=" + restaurantId + "}";
    }

    /**
     * Compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CategoryRequestDTO)) return false;
        CategoryRequestDTO otherObj = (CategoryRequestDTO) obj;
        return Objects.equals(name, otherObj.name) &&
                Objects.equals(restaurantId, otherObj.restaurantId);
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, restaurantId);
    }
}
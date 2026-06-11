package com.naman.capstone.dto.response;

import java.util.Objects;

/**
 * dto representing category response data
 */
public class CategoryResponseDTO {

    /**
     * category id
     */
    private Long id;

    /**
     * name of category
     */
    private String name;

    /**
     * id of restaurant to which category belongs
     */
    private Long restaurantId;



    /**
     * default constructor
     */
    public CategoryResponseDTO() {}

    /**
     * parameterized constructor
     */
    public CategoryResponseDTO(Long id, String name, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.restaurantId = restaurantId;
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
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "CategoryResponseDTO{" +
                "id=" + id +
                ", name='" + name +
                "', restaurantId=" + restaurantId +
                "}";
    }

    /**
     * compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CategoryResponseDTO)) return false;
        CategoryResponseDTO otherObj = (CategoryResponseDTO) obj;
        return Objects.equals(id, otherObj.id) &&
                Objects.equals(name, otherObj.name) &&
                Objects.equals(restaurantId, otherObj.restaurantId);
    }

    /**
     * generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, restaurantId);
    }

}
package com.naman.capstone.dto.request;

import com.naman.capstone.enums.RestaurantStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * DTO for creating or updating a restaurant
 */
public class RestaurantRequestDTO {

    /**
     * Name of the restaurant
     */
    @NotBlank(message = "Restaurant name cannot be empty")
    private String name;

    /**
     * Address of the restaurant
     */
    @NotBlank(message = "Address cannot be empty")
    private String address;

    /**
     * description of restaurant
     */
    private String description;

    /**
     * Current status of the restaurant
     */
    @NotNull(message = "Status is required")
    private RestaurantStatus status;

    /**
     * path of image
     */
    private String imageUrl;



    /**
     * Default constructor
     */
    public RestaurantRequestDTO() {}

    /**
     * Parameterized constructor
     */
    public RestaurantRequestDTO(String name, String address, String description ,RestaurantStatus status, String imageUrl) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
    }


    /**
     * getters setters method
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "RestaurantRequestDTO{" +
                "name='" + name +
                "', address='" + address +
                "', description='" + description +
                "', status=" + status +
                "}";
    }

    /**
     * Compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RestaurantRequestDTO)) return false;

        RestaurantRequestDTO otherObj = (RestaurantRequestDTO) obj;
        return Objects.equals(name, otherObj.name) &&
                Objects.equals(address, otherObj.address) &&
                Objects.equals(description, otherObj.description) &&
                status == otherObj.status;
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, address, description ,status);
    }

}
package com.naman.capstone.dto.response;

import com.naman.capstone.enums.RestaurantStatus;

import java.util.Objects;

/**
 * dto representing restaurant response data
 */
public class RestaurantResponseDTO {

    /**
     * id of restaurant
     */
    private Long id;

    /**
     * restaurant name
     */
    private String name;

    /**
     * address of restaurant
     */
    private String address;

    /**
     * description of restaurant
     */
    private String description;

    /**
     * current status of restaurant
     */
    private RestaurantStatus status;

    /**
     * path of image
     */
    private String imageUrl;



    /**
     * default constructor
     */
    public RestaurantResponseDTO() {}

    /**
     * parameterized constructor
     */
    public RestaurantResponseDTO(Long id, String name, String address, String description, RestaurantStatus status, String imageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
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
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "RestaurantResponseDTO{" +
                "id=" + id +
                ", name='" + name +
                "', address='" + address +
                "', description='" + description +
                "', status=" + status +
                '}';
    }

    /**
     * compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof RestaurantResponseDTO)) return false;

        RestaurantResponseDTO otherObj = (RestaurantResponseDTO) obj;
        return Objects.equals(id, otherObj.id) &&
                Objects.equals(name, otherObj.name) &&
                Objects.equals(address, otherObj.address) &&
                Objects.equals(description, otherObj.description) &&
                status == otherObj.status;
    }

    /**
     * generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, description, status);
    }

}
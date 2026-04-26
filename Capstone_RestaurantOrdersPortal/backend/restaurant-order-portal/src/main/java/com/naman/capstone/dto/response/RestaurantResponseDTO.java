package com.naman.capstone.dto.response;

import com.naman.capstone.enums.RestaurantStatus;

import java.util.Objects;

/**
 * Represents the restaurant data sent back to owner in API response
 */
public class RestaurantResponseDTO {

    private Long id;
    private String name;
    private String address;
    private RestaurantStatus status;


    //constructors
    public RestaurantResponseDTO() {}

    public RestaurantResponseDTO(Long id, String name, String address, RestaurantStatus status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = status;
    }


    //getters setters
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

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }


    // toString()
    @Override
    public String toString() {
        return "RestaurantResponseDTO{" +
                "id=" + id +
                ", name='" + name +
                "', address='" + address +
                "', status=" + status +
                '}';
    }

    // equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof RestaurantResponseDTO)) return false;

        RestaurantResponseDTO otherObj = (RestaurantResponseDTO) obj;
        return Objects.equals(id, otherObj.id) &&
                Objects.equals(name, otherObj.name) &&
                Objects.equals(address, otherObj.address) &&
                status == otherObj.status;
    }

    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, status);
    }

}
package com.naman.capstone.dto.request;

import com.naman.capstone.enums.RestaurantStatus;
import com.naman.capstone.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents the data received from owner for creating or updating a restaurant
 */
public class RestaurantRequestDTO {

    @NotBlank(message = "Restaurant name cannot be empty")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotNull(message = "Status is required")
    private RestaurantStatus status;


    //constructors
    public RestaurantRequestDTO() {}

    public RestaurantRequestDTO(String name, String address, RestaurantStatus status) {
        this.name = name;
        this.address = address;
        this.status = status;
    }


    //getters setters
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


    //toString
    @Override
    public String toString() {
        return "RestaurantRequestDTO{" +
                "name='" + name +
                "', address='" + address + 
                "', status=" + status +
                "}";
    }

    //equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RestaurantRequestDTO)) return false;

        RestaurantRequestDTO otherObj = (RestaurantRequestDTO) obj;
        return Objects.equals(name, otherObj.name) &&
                Objects.equals(address, otherObj.address) &&
                status == otherObj.status;
    }

    //hashCode
    @Override
    public int hashCode() {
        return Objects.hash(name, address, status);
    }

}
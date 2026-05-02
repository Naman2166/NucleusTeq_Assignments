package com.naman.capstone.dto.response;

import java.util.Objects;

/**
 * DTO representing address response data
 */
public class AddressResponseDTO {

    /**
     * id of address
     */
    private Long id;

    /**
     * Street name
     */
    private String street;

    /**
     * City name
     */
    private String city;

    /**
     * State of address
     */
    private String state;

    /**
     * Pincode number
     */
    private String pincode;


    /**
     * Default constructor
     */
    public AddressResponseDTO() {}

    /**
     * Parameterized constructor
     */
    public AddressResponseDTO(Long id, String street, String city, String state, String pincode) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }


    /**
     * Getters and setters for all fields
     */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }


    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "AddressResponseDTO{" +
                "id=" + id +
                ", street='" + street +
                "', city='" + city +
                "', state='" + state +
                "', pincode='" + pincode +
                "'}";
    }

    /**
     * Compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AddressResponseDTO)) return false;
        AddressResponseDTO other = (AddressResponseDTO) obj;
        return Objects.equals(id, other.id);
    }

    /**
     * Generates hash code based on id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
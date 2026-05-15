package com.naman.capstone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

/**
 * Request DTO for address related operations
 */
public class AddressRequestDTO {

    /**
     * Street address
     */
    @NotBlank(message = "Street is required")
    @Pattern(regexp = "^[A-Za-z. ]+$", message = "street address must contain only letters")
    private String street;

    /**
     * City name
     */
    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "City must contain only letters")
    private String city;

    /**
     * State name
     */
    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[A-Za-z. ]+$", message = "State must contain only letters")
    private String state;

    /**
     * pincode of city
     */
    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must contain exactly 6 Numbers")
    private String pincode;



    /**
     * Default constructor.
     */
    public AddressRequestDTO() {}

    /**
     * Parameterized constructor
     */
    public AddressRequestDTO(String street, String city, String state, String pincode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }


    /**
     * Getter setter methods
     */
    public String getStreet() { return street; }

    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }

    public void setPincode(String pincode) { this.pincode = pincode; }


    /**
     * Returns string representation of AddressRequestDTO
     */
    @Override
    public String toString() {
        return "AddressRequestDTO{" +
                "street='" + street +
                "', city='" + city +
                "', state='" + state +
                "', pincode='" + pincode +
                "'}";
    }

    /**
     * Compares this object with another for equality
     * @param obj object to compare
     * @return true if equal otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AddressRequestDTO)) return false;
        AddressRequestDTO other = (AddressRequestDTO) obj;
        return Objects.equals(street, other.street) &&
                Objects.equals(city, other.city) &&
                Objects.equals(state, other.state) &&
                Objects.equals(pincode, other.pincode);
    }

    /**
     * it generates hash code for this DTO object
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, pincode);
    }
}
package com.naman.capstone.dto.response;

import com.naman.capstone.enums.Role;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents the data sent back to the client after user operations
 */
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private BigDecimal walletBalance;


    //constructors
    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String firstName, String lastName, String email, String phoneNumber, Role role, BigDecimal walletBalance) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.walletBalance = walletBalance;
    }


    //getters setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(BigDecimal walletBalance) {
        this.walletBalance = walletBalance;
    }


    // toString
    @Override
    public String toString() {
        return "UserResponseDTO{" + "id=" + id +
                ", firstName='" + firstName +
                "', lastName='" + lastName +
                "', email='" + email +
                "', phoneNumber='" + phoneNumber +
                "', role=" + role +
                ", walletBalance=" + walletBalance +
                "}";
    }

    // equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserResponseDTO)) return false;

        UserResponseDTO otherObj = (UserResponseDTO) obj;

        return Objects.equals(id, otherObj.id) &&
                Objects.equals(firstName, otherObj.firstName) &&
                Objects.equals(lastName, otherObj.lastName) &&
                Objects.equals(email, otherObj.email) &&
                Objects.equals(phoneNumber, otherObj.phoneNumber) &&
                role == otherObj.role &&
                Objects.equals(walletBalance, otherObj.walletBalance);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, role, walletBalance);
    }

}


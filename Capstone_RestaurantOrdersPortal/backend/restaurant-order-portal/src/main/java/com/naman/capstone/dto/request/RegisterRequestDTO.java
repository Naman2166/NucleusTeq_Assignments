package com.naman.capstone.dto.request;

import com.naman.capstone.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

/**
 * Represents the data received from user for registration
 */
public class RegisterRequestDTO {

    /**
     * User first name
     */
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    /**
     * User last name
     */
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    /**
     * User email address
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * Userpassword
     */
    @NotBlank(message = "Password cannot be empty")
    private String password;

    /**
     * User phone number
     * must be 10 digits
     */
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    /**
     * Role of user
     */
    @NotNull(message = "Role is required")
    private Role role;


    /**
     * Default constructor
     */
    public RegisterRequestDTO() {}

    /**
     * Parameterized constructor
     */
    public RegisterRequestDTO(String firstName, String lastName, String email, String password, String phoneNumber, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    /**
     * getter setter
     */
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


    /**
     * Returns string representation of object
     */
    @Override
    public String toString() {
        return "RegisterRequestDTO{" +
                "firstName='" + firstName +
                "', lastName='" + lastName +
                "', email='" + email +
                "', phoneNumber='" + phoneNumber +
                "', role=" + role +
                ", password='[PROTECTED]'" +
                "}";
    }

    /**
     * Compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RegisterRequestDTO)) return false;

        RegisterRequestDTO otherObj = (RegisterRequestDTO) obj;

        return Objects.equals(firstName, otherObj.firstName) &&
                Objects.equals(lastName, otherObj.lastName) &&
                Objects.equals(email, otherObj.email) &&
                Objects.equals(password, otherObj.password) &&
                Objects.equals(phoneNumber, otherObj.phoneNumber) &&
                role == otherObj.role;
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, phoneNumber, role);
    }
}
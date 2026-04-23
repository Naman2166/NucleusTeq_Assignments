package com.naman.capstone.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

/**
 * Represents the data received from the client for login
 */
public class LoginRequestDTO {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;


    //constructor
    public LoginRequestDTO() {}

    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }


    //getter setter
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


    //toString
    @Override
    public String toString() {
        return "LoginRequestDTO{" + "email='" + email + "', password='[PROTECTED]'}";
    }

    //equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LoginRequestDTO)) return false;

        LoginRequestDTO otherObj = (LoginRequestDTO) obj;

        return Objects.equals(email, otherObj.email) &&
                Objects.equals(password, otherObj.password);
    }

    //hashCode
    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

}

package com.naman.capstone.dto.response;

import com.naman.capstone.enums.Role;

import java.util.Objects;

/**
 * dto representing login response data
 */
public class LoginResponseDTO {

    /**
     * jwt token for authenticated user
     */
    private String token;

    /**
     * user details of logged in user
     */
    private UserResponseDTO user;

    /**
     * role of the user
     */
    private Role role;



    /**
     * default constructor
     */
    public LoginResponseDTO() {}

    /**
     * parameterized constructor
     */
    public LoginResponseDTO(String token, UserResponseDTO user, Role role) {
        this.token = token;
        this.user = user;
        this.role = role;
    }



    /**
     * getters and setters for all fields
     */
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "LoginResponseDTO{" + "token='[PROTECTED]'" + ", user=" + user + ", role=" + role + "}";
    }

    /**
     * compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LoginResponseDTO)) return false;

        LoginResponseDTO otherObj = (LoginResponseDTO) obj;

        return Objects.equals(token, otherObj.token) &&
                Objects.equals(user, otherObj.user) &&
                role == otherObj.role;
    }

    /**
     * generates hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(token, user, role);
    }
}
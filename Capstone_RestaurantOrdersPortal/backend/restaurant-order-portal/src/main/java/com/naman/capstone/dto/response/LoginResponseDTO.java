package com.naman.capstone.dto.response;


import com.naman.capstone.enums.Role;

import java.util.Objects;

/**
 * Represents the data sent back to client after login
 */
public class LoginResponseDTO {

    private String token;
    private UserResponseDTO user;
    private Role role;


    //Constructors
    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, UserResponseDTO user, Role role) {
        this.token = token;
        this.user = user;
        this.role = role;
    }


    //getters setters
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


    //toString
    @Override
    public String toString() {
        return "LoginResponseDTO{" + "token='[PROTECTED]'" + ", user=" + user + ", role=" + role + "}";
    }

    //equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LoginResponseDTO)) return false;

        LoginResponseDTO otherObj = (LoginResponseDTO) obj;

        return Objects.equals(token, otherObj.token) &&
                Objects.equals(user, otherObj.user) &&
                role == otherObj.role;
    }

    //hashCode
    @Override
    public int hashCode() {
        return Objects.hash(token, user, role);
    }
}

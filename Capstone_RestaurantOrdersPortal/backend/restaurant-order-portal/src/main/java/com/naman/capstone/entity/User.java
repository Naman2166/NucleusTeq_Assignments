package com.naman.capstone.entity;

import com.naman.capstone.enums.Role;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * entity representing user data with is mapped to users table in database
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * id of user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * first name of user
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * last name of user
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * email of user
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * password
     */
    @Column(nullable = false)
    private String password;

    /**
     * phone number of user
     */
    @Column(nullable = false)
    private String phoneNumber;

    /**
     * user role
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * wallet balance of user
     */
    @Column(nullable = false)
    private BigDecimal walletBalance = BigDecimal.ZERO;

    /**
     * account creation time
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    /**
     * default constructor
     */
    public User(){
    }

    /**
     * parameterized constructor
     */
    public User(String firstName, String lastName, String password, String email, String phoneNumber, Role role, BigDecimal walletBalance){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.walletBalance = walletBalance;
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

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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


    /**
     * sets created time before saving to database
     */
    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id +
                ", firstName='" + firstName +
                "', lastName='" + lastName +
                "', password='[PROTECTED]'" +
                ", email='" + email +
                "', role=" + role +
                ", walletBalance=" + walletBalance +
                ", createdAt=" + createdAt +
                "}";
    }

    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User otherObj = (User) obj;
        return id != null && id.equals(otherObj.id);
    }

    /**
     * generates hash code
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
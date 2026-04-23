package com.naman.capstone.entity;

import com.naman.capstone.enums.Role;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This class represents user entity which is mapped to database table
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                     //here id generates automatically

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)         //stores enum in text format in databse
    private Role role;

    @Column(nullable = false)
    private BigDecimal walletBalance = BigDecimal.ZERO;      //initializes walletBalance with 0 to avoid null error

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    //default constructor
    public User(){
    }

    //parametrized constructor
    public User(String firstName, String lastName, String password, String email, String phoneNumber, Role role, BigDecimal walletBalance){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.walletBalance = walletBalance;
    }


    //Getter Setter
    public Long getId() {
        return id;
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

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    //toString
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
                '}';
    }

    //equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User otherObj = (User) obj;
        return id != null && id.equals(otherObj.id);
    }

    //hashCode
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
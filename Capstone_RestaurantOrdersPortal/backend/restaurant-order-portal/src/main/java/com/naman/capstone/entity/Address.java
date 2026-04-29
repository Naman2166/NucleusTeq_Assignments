package com.naman.capstone.entity;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * entity representing user address data
 */
@Entity
@Table(name = "addresses")
public class Address {

    /**
     * id of address
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * user who owns this address
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * street name
     */
    @Column(nullable = false)
    private String street;

    /**
     * city name
     */
    @Column(nullable = false)
    private String city;

    /**
     * state of address
     */
    @Column(nullable = false)
    private String state;

    /**
     * pincode number
     */
    @Column(nullable = false)
    private String pincode;


    /**
     * default constructor
     */
    public Address() {}

    /**
     * parameterized constructor
     */
    public Address(User user, String street, String city, String state, String pincode) {
        this.user = user;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }


    /**
     * getters and setters for all fields
     */
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "Address{" + "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", street='" + street +
                "', city='" + city +
                "', state='" + state +
                "', pincode='" + pincode +
                "'}";
    }

    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Address)) return false;
        Address other = (Address) obj;
        return Objects.equals(id, other.id);
    }

    /**
     * generates hash code based on id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
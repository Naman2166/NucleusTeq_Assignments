package com.naman.capstone.entity;

import com.naman.capstone.enums.RestaurantStatus;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * entity representing restaurant data
 */
@Entity
@Table(name = "restaurants")
public class Restaurant {

    /**
     * id of restaurant
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * restaurant name
     */
    @Column(nullable = false)
    private String name;

    /**
     * address of restaurant
     */
    @Column(nullable = false)
    private String address;

    /**
     * current status of restaurant
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    /**
     * owner of restaurant
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * url of image file path
     */
    @Column(name = "image_url")
    private String imageUrl;



    /**
     * default constructor
     */
    public Restaurant() {}

    /**
     * parameterized constructor
     */
    public Restaurant(String name, String address, RestaurantStatus status, User owner, String imageUrl) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.owner = owner;
        this.imageUrl = imageUrl;
    }


    /**
     * getters and setters for all fields
     */
    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public RestaurantStatus getStatus(){
        return status;
    }

    public void setStatus(RestaurantStatus status){
        this.status = status;
    }

    public User getOwner(){
        return owner;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "Restaurant{" + "id=" + id +
                ", name='" + name +
                "', address='" + address +
                "', status=" + status +
                "}";
    }


    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant otherObj = (Restaurant) o;
        return Objects.equals(id, otherObj.id);
    }

    /**
     * generates hash code based on id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
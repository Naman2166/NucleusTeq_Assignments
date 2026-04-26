package com.naman.capstone.entity;

import com.naman.capstone.enums.RestaurantStatus;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * This class represents restaurant entity which is mapped to restaurants table in database
 */
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    @ManyToOne(fetch = FetchType.LAZY)                     //many restaurants can belongs to one owner
    @JoinColumn(name = "owner_id", nullable = false)       //it creates owner_id as foreign key which relates to primary key in User table
    private User owner;


    //constructors
    public Restaurant() {}

    public Restaurant(String name, String address, RestaurantStatus status, User owner) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.owner = owner;
    }


    //getters setters
    public Long getId(){
        return id;
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



    //toString
    @Override
    public String toString() {
        return "Restaurant{" + "id=" + id +
                ", name='" + name +
                "', address='" + address +
                "', status=" + status +
                "}";
    }


    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant otherObj = (Restaurant) o;
        return Objects.equals(id, otherObj.id);
    }

    //hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
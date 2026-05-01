package com.naman.capstone.entity;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * represents a category inside a restaurant
 */
@Entity
@Table(name = "categories")
public class Category {

    /**
     * category id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * name of category
     */
    @Column(nullable = false)
    private String name;

    /**
     * restaurant to which this category belongs
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;


    /**
     * default constructor
     */
    public Category() {}

    /**
     * parameterized constructor
     */
    public Category(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name='" + name + "'}";
    }

    /**
     * compares another object with this for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category)) return false;
        Category otherObj = (Category) obj;
        return id != null && Objects.equals(id, otherObj.id);
    }

    /**
     * generates hash code based on id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
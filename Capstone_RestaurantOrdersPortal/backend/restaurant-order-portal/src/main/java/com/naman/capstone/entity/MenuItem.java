package com.naman.capstone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * entity representing menu item which are inside a category
 */
@Entity
@Table(name = "menu_items")
public class MenuItem {

    /**
     * id of menu item
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * name of menu item
     */
    @Column(nullable = false)
    private String name;

    /**
     * price of menu item
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * category to which this item belongs
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * restaurant to which this item belongs
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    /**
     * path of image
     */
    private String imageUrl;



    /**
     * default constructor
     */
    public MenuItem() {}

    /**
     * parameterized constructor
     */
    public MenuItem(String name, BigDecimal price, Category category, Restaurant restaurant, String imageUrl) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
        this.imageUrl = imageUrl;
    }



    /**
     * getters and setters for all fields
     */
    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
        return "MenuItem{" + "id=" + id + ", name='" + name + "', price=" + price + "}";
    }

    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MenuItem)) return false;
        MenuItem otherObj = (MenuItem) obj;
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
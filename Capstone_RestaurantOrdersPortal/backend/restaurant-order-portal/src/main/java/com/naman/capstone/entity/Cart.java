package com.naman.capstone.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * entity representing shopping cart of user
 */
@Entity
@Table(name = "carts")
public class Cart {

    /**
     * id of cart
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * user who owns this cart
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * list of items in cart
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    /**
     * total price of all items in cart
     */
    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;


    /**
     * default constructor
     */
    public Cart() {}

    /**
     * parameterized constructor
     */
    public Cart(User user) {
        this.user = user;
        this.totalPrice = BigDecimal.ZERO;
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

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    /**
     * adds item to cart and set relation with cartItem
     */
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    /**
     * removes item from cart and remove relation with carItem
     */
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }


    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", totalPrice=" + totalPrice +
                "}";
    }

    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cart)) return false;
        Cart cart = (Cart) obj;
        return Objects.equals(id, cart.id);
    }

    /**
     * generates hash code based on id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
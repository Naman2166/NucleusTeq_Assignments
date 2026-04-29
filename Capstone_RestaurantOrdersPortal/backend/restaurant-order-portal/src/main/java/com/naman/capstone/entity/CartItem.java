package com.naman.capstone.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * entity representing cart item data
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    /**
     * id of cart item
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * cart to which this item belongs
     */
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * menu item added to cart
     */
    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    /**
     * quantity of item
     */
    @Column(nullable = false)
    private int quantity;

    /**
     * price of single item
     */
    @Column(nullable = false)
    private BigDecimal unitPrice;

    /**
     * total price of item
     */
    @Column(nullable = false)
    private BigDecimal totalPrice;



    /**
     * default constructor
     */
    public CartItem() {}

    /**
     * parameterized constructor
     */
    public CartItem(Cart cart, MenuItem menuItem, int quantity, BigDecimal unitPrice) {
        this.cart = cart;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }


    /**
     * getters and setters for all fields
     */
    public Long getId() {
        return id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }


    /**
     * calculates total price using quantity and unit price
     */
    public void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", cartId=" + (cart != null ? cart.getId() : null) +
                ", menuItemId=" + (menuItem != null ? menuItem.getId() : null) +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }

    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) obj;
        return Objects.equals(id, cartItem.id);
    }

    /**
     * generates hash code based on id
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
package com.naman.capstone.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * entity represents an item inside a order
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    /**
     * id of order item
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * order to which this item belongs
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * menu item which is ordered
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
     * price of single order item
     */
    @Column(nullable = false)
    private BigDecimal unitPrice;

    /**
     * total price of item
     * totalPrice = unitPrice x quantity
     */
    @Column(nullable = false)
    private BigDecimal totalPrice;


    /**
     * default constructor
     */
    public OrderItem() {}

    /**
     * parameterized constructor
     */
    public OrderItem(Order order, MenuItem menuItem, int quantity, BigDecimal unitPrice) {
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }


    /**
     * method to calculates total price using quantity and unit price
     */
    public void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }


    /**
     * getters and setters for all fields
     */
    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "OrderItem{" + "id=" + id +
                ", orderId=" + (order != null ? order.getId() : null) +
                ", menuItemId=" + (menuItem != null ? menuItem.getId() : null) +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                "}";
    }

    /**
     * compares objects based on id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderItem)) return false;
        OrderItem otherObj = (OrderItem) obj;
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
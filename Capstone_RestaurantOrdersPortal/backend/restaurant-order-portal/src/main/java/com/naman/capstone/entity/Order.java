package com.naman.capstone.entity;

import com.naman.capstone.enums.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * entity representing order data
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * id of order
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * user who placed the order
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * restaurant from which order is placed
     */
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    /**
     * list of items in order
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    /**
     * total price of order
     */
    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * current status of order
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PLACED;

    /**
     * time when order was placed
     */
    @Column(nullable = false)
    private LocalDateTime orderTime = LocalDateTime.now();

    /**
     * address for delivery
     */
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;



    /**
     * default constructor
     */
    public Order() {}

    /**
     * parameterized constructor
     */
    public Order(User user, Restaurant restaurant, Address address) {
        this.user = user;
        this.restaurant = restaurant;
        this.address = address;
        this.status = OrderStatus.PLACED;
        this.orderTime = LocalDateTime.now();
        this.totalPrice = BigDecimal.ZERO;
    }


    /**
     * method to adds item in order and sets relation with orderItem
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * method to removes item from order and clears relation
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    /**
     * calculates total price of order
     */
    public void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getTotalPrice());
        }
        this.totalPrice = total;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items.clear();
        for (OrderItem item : items) {
            addItem(item);
        }
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "Order{" + "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", restaurantId=" + (restaurant != null ? restaurant.getId() : null) +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", orderTime=" + orderTime +
                '}';
    }

    /**
     * compares other object with this object for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;
        Order otherObj = (Order) obj;
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
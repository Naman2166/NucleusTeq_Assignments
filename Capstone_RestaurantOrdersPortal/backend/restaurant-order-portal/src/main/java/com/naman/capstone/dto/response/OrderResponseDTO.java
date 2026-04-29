package com.naman.capstone.dto.response;

import com.naman.capstone.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * dto representing order response data
 */
public class OrderResponseDTO {

    /**
     * id of order
     */
    private Long orderId;

    /**
     * name of restaurant
     */
    private String restaurantName;

    /**
     * total price of order
     */
    private BigDecimal totalPrice;

    /**
     * current status of order
     */
    private OrderStatus status;

    /**
     * time when order was placed
     */
    private LocalDateTime orderTime;

    /**
     * list of all items in order
     */
    private List<OrderItemResponseDTO> items;


    /**
     * default constructor
     */
    public OrderResponseDTO() {}

    /**
     * parameterized constructor
     */
    public OrderResponseDTO(Long orderId, String restaurantName, BigDecimal totalPrice,
                            OrderStatus status, LocalDateTime orderTime,
                            List<OrderItemResponseDTO> items) {
        this.orderId = orderId;
        this.restaurantName = restaurantName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderTime = orderTime;
        this.items = items;
    }


    /**
     * getters and setters for all fields
     */
    public Long getOrderId() {
        return orderId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }


    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "OrderResponseDTO{" +
                "orderId=" + orderId +
                ", restaurantName='" + restaurantName +
                "', totalPrice=" + totalPrice +
                ", status=" + status +
                ", orderTime=" + orderTime +
                ", items=" + items +
                "}";
    }

    /**
     * compares objects based on orderId
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderResponseDTO)) return false;
        OrderResponseDTO other = (OrderResponseDTO) obj;
        return Objects.equals(orderId, other.orderId);
    }

    /**
     * generates hash code based on orderId
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
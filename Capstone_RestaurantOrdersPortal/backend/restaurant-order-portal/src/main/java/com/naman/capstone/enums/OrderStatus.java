package com.naman.capstone.enums;

/**
 * enum representing current status of order
 */
public enum OrderStatus {

    /**
     * order is placed successfully
     */
    PLACED,

    /**
     * order is waiting for processing
     */
    PENDING,

    /**
     * order is delivered to customer
     */
    DELIVERED,

    /**
     * order is completed
     */
    COMPLETED,

    /**
     * order is cancelled
     */
    CANCELLED
}
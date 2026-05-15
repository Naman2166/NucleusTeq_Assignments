package com.naman.capstone.repository;

import com.naman.capstone.entity.Order;
import com.naman.capstone.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for OrderItem entity operations
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Finds all items inside a order
     * @param order order
     * @return list of order items
     */
    List<OrderItem> findByOrder(Order order);

}
package com.naman.capstone.repository;

import com.naman.capstone.entity.Order;
import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import com.naman.capstone.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Order entity operations
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all the orders of a user
     * @param user logged-in user
     * @return list of orders
     */
    List<Order> findByUser(User user);

    /**
     * Finds all orders of a user by status
     * @param user logged-in user
     * @param status order status
     * @return list of orders
     */
    List<Order> findByUserAndStatus(User user, OrderStatus status);

    /**
     * Finds all orders for a restaurant
     * @param restaurant the restaurant
     * @return list of orders
     */
    List<Order> findByRestaurant(Restaurant restaurant);


}
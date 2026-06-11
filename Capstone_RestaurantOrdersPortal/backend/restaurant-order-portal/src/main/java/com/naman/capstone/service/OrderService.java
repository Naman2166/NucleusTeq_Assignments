package com.naman.capstone.service;

import com.naman.capstone.dto.response.OrderResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.enums.OrderStatus;

import java.util.List;

/**
 * Defines order related operations
 */
public interface OrderService {

    /**
     * place order from cart
     * @param user logged-in user
     * @param addressId id of address used for placing order
     * @return created order data
     */
    OrderResponseDTO placeOrder(User user, Long addressId);

    /**
     * cancel order by user
     * @param user logged-in user
     * @param orderId id of the order
     */
    void cancelOrder(User user, Long orderId);

    /**
     * cancel order by restaurant owner
     * @param orderId id of the order
     * @param owner the restaurant owner
     */
    void cancelOrderByOwner(Long orderId, User owner);

    /**
     * get order by id
     * @param user logged-in user
     * @param orderId id of the order
     * @return order data
     */
    OrderResponseDTO getOrderById(User user, Long orderId);

    /**
     * get order history of user
     * @param user logged-in user
     * @return list of orders
     */
    List<OrderResponseDTO> getUserOrders(User user);

    /**
     * get all orders from a specific restaurant
     * @param restaurantId id of the restaurant
     * @param restaurantOwner the restaurant owner
     * @return list of orders
     */
    List<OrderResponseDTO> getOrdersByRestaurant(Long restaurantId, User restaurantOwner);

    /**
     * update order status
     * @param orderId id of the order
     * @param status new order status
     * @param restaurantOwner the restaurant owner
     * @return updated order data
     */
    OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status, User restaurantOwner);
}
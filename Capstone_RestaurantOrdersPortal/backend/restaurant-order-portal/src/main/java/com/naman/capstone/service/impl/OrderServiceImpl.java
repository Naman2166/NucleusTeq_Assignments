package com.naman.capstone.service.impl;

import com.naman.capstone.dto.response.OrderResponseDTO;
import com.naman.capstone.entity.*;
import com.naman.capstone.enums.OrderStatus;
import com.naman.capstone.exception.*;
import com.naman.capstone.mapper.OrderMapper;
import com.naman.capstone.repository.*;
import com.naman.capstone.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * service implementation for managing orders
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public OrderServiceImpl(CartRepository cartRepository,
                            OrderRepository orderRepository,
                            RestaurantRepository restaurantRepository,
                            UserRepository userRepository,
                            AddressRepository addressRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * place order
     * @param user the user
     * @param addressId id of address
     * @return created order
     */
    @Override
    public OrderResponseDTO placeOrder(User user, Long addressId) {

        logger.info("placing order for user id {}", user.getId());
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        Restaurant restaurant = cart.getItems().get(0).getMenuItem().getRestaurant();

        for (CartItem item : cart.getItems()) {
            if (!item.getMenuItem().getRestaurant().getId().equals(restaurant.getId())) {
                throw new MultipleRestaurantCartException("Cart contains multiple restaurants");
            }
        }

        BigDecimal total = cart.getTotalPrice();

        if (user.getWalletBalance().compareTo(total) < 0) {
            throw new InsufficientBalanceException("Insufficient wallet balance");
        }

        user.setWalletBalance(user.getWalletBalance().subtract(total));
        userRepository.save(user);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Order order = new Order(user, restaurant, address);

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.getMenuItem(),
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
            order.addItem(orderItem);
        }

        order.calculateTotal();

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        logger.info("order placed successfully with id {}", savedOrder.getId());

        return OrderMapper.toDTO(savedOrder);
    }


    /**
     * cancel order by user
     * @param user the user
     * @param orderId id of order
     */
    @Override
    public void cancelOrder(User user, Long orderId) {

        logger.info("user id {} attempting to cancel order {}", user.getId(), orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new OrderNotCancellableException("Order cannot be cancelled");
        }

        long seconds = Duration.between(order.getOrderTime(), LocalDateTime.now()).getSeconds();

        if (seconds > 30) {
            throw new OrderCancellationTimeExceededException("Cancellation time exceeded");
        }

        order.setStatus(OrderStatus.CANCELLED);
        user.setWalletBalance(user.getWalletBalance().add(order.getTotalPrice()));
        userRepository.save(user);
        logger.info("order id {} cancelled successfully by user id {}", orderId, user.getId());
    }


    /**
     * cancel order by restaurant owner
     * @param orderId id of order
     * @param owner restaurant owner
     */
    @Override
    public void cancelOrderByOwner(Long orderId, User owner) {

        logger.info("owner id {} attempting to cancel order id {}", owner.getId(), orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getRestaurant().getOwner().getId().equals(owner.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        order.setStatus(OrderStatus.CANCELLED);
        logger.info("order id {} cancelled by owner {}", orderId, owner.getId());
    }


    /**
     * get order by id
     * @param user the user
     * @param orderId id of order
     * @return order data
     */
    @Override
    public OrderResponseDTO getOrderById(User user, Long orderId) {

        logger.info("fetching order id {} for user id {}", orderId, user.getId());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        return OrderMapper.toDTO(order);
    }


    /**
     * get all orders of user
     * @param user the user
     * @return list of orders
     */
    @Override
    public List<OrderResponseDTO> getUserOrders(User user) {

        logger.info("fetching orders for user {}", user.getId());
        List<Order> orders = orderRepository.findByUser(user);

        List<OrderResponseDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(OrderMapper.toDTO(order));
        }
        return result;
    }


    /**
     * get orders by restaurant
     * @param restaurantId id of restaurant
     * @param restaurantOwner the owner
     * @return list of orders
     */
    @Override
    public List<OrderResponseDTO> getOrdersByRestaurant(Long restaurantId, User restaurantOwner) {

        logger.info("fetching orders for restaurant {}", restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!restaurant.getOwner().getId().equals(restaurantOwner.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        List<Order> orders = orderRepository.findByRestaurant(restaurant);
        List<OrderResponseDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(OrderMapper.toDTO(order));
        }

        return result;
    }


    /**
     * update order status
     * @param orderId id of order
     * @param status new status
     * @param restaurantOwner the owner
     * @return updated order
     */
    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status, User restaurantOwner) {

        logger.info("updating order id {} status to {}", orderId, status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getRestaurant().getOwner().getId().equals(restaurantOwner.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        order.setStatus(status);
        return OrderMapper.toDTO(order);
    }
}
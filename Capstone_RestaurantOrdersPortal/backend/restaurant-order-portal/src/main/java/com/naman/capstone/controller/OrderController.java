package com.naman.capstone.controller;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.OrderRequestDTO;
import com.naman.capstone.dto.response.OrderResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.enums.OrderStatus;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.naman.capstone.constant.AppConstants.*;

/**
 * REST controller for handling order related API
 */
@RestController
@RequestMapping(AppConstants.ORDER_BASE_URL)
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public OrderController(OrderService orderService, CurrentUserService currentUserService) {
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }


    /**
     * Place a new order from user cart
     * @param requestDTO contains addressId for placing order
     * @param userDetails authenticated user details
     * @return created order details
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @RequestBody @Valid OrderRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Place order request received");
        User user = currentUserService.getCurrentUser(userDetails);
        OrderResponseDTO response = orderService.placeOrder(user, requestDTO.getAddressId());
        log.info("Order placed successfully for userId={}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Cancel order by the user
     * @param orderId  id of the order to cancel
     * @param userDetails authenticated user details
     * @return no content response
     */
    @PutMapping(ORDER_ID + CANCEL)
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        log.warn("Cancel order request received for orderId={}", orderId);
        User user = currentUserService.getCurrentUser(userDetails);
        orderService.cancelOrder(user, orderId);
        log.info("Order cancelled successfully by user for orderId={}", orderId);

        return ResponseEntity.noContent().build();
    }


    /**
     * Cancels an order by the restaurant owner.
     * @param orderId  id of the order
     * @param userDetails  authenticated owner details
     * @return no content response
     */
    @PutMapping(ORDER_ID + CANCEL_BY_OWNER)
    public ResponseEntity<Void> cancelOrderByOwner(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.warn("Cancel order by owner request received for orderId={}", orderId);
        User owner = currentUserService.getCurrentUser(userDetails);
        orderService.cancelOrderByOwner(orderId, owner);
        log.info("Order cancelled successfully by owner for orderId={}", orderId);

        return ResponseEntity.noContent().build();
    }


    /**
     * Retrieves an order by id
     * @param orderId order id
     * @param userDetails authenticated user details
     * @return order details
     */
    @GetMapping(ORDER_ID)
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Fetching order for orderId={}", orderId);
        User user = currentUserService.getCurrentUser(userDetails);
        OrderResponseDTO response = orderService.getOrderById(user, orderId);
        log.info("Order fetched successfully for orderId={}", orderId);

        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves all orders of a authenticated user
     * @param userDetails authenticated user details
     * @return list of user orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Fetching order history for current user");
        User user = currentUserService.getCurrentUser(userDetails);
        List<OrderResponseDTO> response = orderService.getUserOrders(user);
        log.info("Fetched orders for userId={}", user.getId());

        return ResponseEntity.ok(response);
    }


    /**
     * Retrieves all orders for a specific restaurant
     * @param restaurantId restaurant id
     * @param userDetails authenticated owner details
     * @return list of orders for restaurant
     */
    @GetMapping(RESTAURANT + RESTAURANT_ID)
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByRestaurant(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Fetching orders for restaurantId={}", restaurantId);
        User owner = currentUserService.getCurrentUser(userDetails);
        List<OrderResponseDTO> response = orderService.getOrdersByRestaurant(restaurantId, owner);
        log.info("Fetched orders for restaurantId={}", restaurantId);

        return ResponseEntity.ok(response);
    }


    /**
     * Update status of an order
     * @param orderId order id
     * @param status new order status
     * @param userDetails authenticated owner details
     * @return updated order details
     */
    @PutMapping(ORDER_ID + STATUS)
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Update order status request received for orderId={} with status={}", orderId, status);
        User owner = currentUserService.getCurrentUser(userDetails);
        OrderResponseDTO response = orderService.updateOrderStatus(orderId, status, owner);
        log.info("Order status updated successfully for orderId={}", orderId);

        return ResponseEntity.ok(response);
    }
}
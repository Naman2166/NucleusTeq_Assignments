package com.naman.capstone.controller;

import com.naman.capstone.dto.request.CartRequestDTO;
import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.service.CartService;
import com.naman.capstone.service.CurrentUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.naman.capstone.constant.AppConstants.*;

/**
 * REST controller for managing cart operations
 */
@RestController
@RequestMapping(CART_BASE_URL)
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;
    private final CurrentUserService currentUserService;

    public CartController(CartService cartService, CurrentUserService currentUserService) {
        this.cartService = cartService;
        this.currentUserService = currentUserService;
    }


    /**
     * Add item in user cart
     * @param requestDTO contains menu item ID and quantity
     * @param userDetails authenticated user details
     * @return updated cart response
     */
    @PostMapping(CART_ITEMS)
    public ResponseEntity<CartResponseDTO> addItem(
            @Valid @RequestBody CartRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Request received to add item in cart");
        User user = currentUserService.getCurrentUser(userDetails);

        CartResponseDTO response = cartService.addItem(user, requestDTO.getMenuItemId(), requestDTO.getQuantity());

        log.info("Item added to cart successfully for userId={}", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Fetching user cart details
     * @param userDetails authenticated user details
     * @return cart details
     */
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Fetching cart for current user");
        User user = currentUserService.getCurrentUser(userDetails);

        CartResponseDTO response = cartService.getCart(user);
        log.info("Cart fetched successfully for userId={}", user.getId());
        return ResponseEntity.ok(response);
    }


    /**
     * Removes a specific item from the user's cart
     * @param cartItemId id of the cart item to remove
     * @param userDetails authenticated user details
     * @return no content response
     */
    @DeleteMapping(CART_ITEMS + CART_ITEM_ID)
    public ResponseEntity<Void> removeItem(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.warn("Remove item request received for cartItemId={}", cartItemId);
        User user = currentUserService.getCurrentUser(userDetails);

        cartService.removeItem(user, cartItemId);

        log.info("Item removed successfully from cart");
        return ResponseEntity.noContent().build();
    }


    /**
     * Clear all items from the user's cart
     * @param userDetails authenticated user details
     * @return no content response
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.warn("Clear cart request received");
        User user = currentUserService.getCurrentUser(userDetails);

        cartService.clearCart(user);

        log.info("Cart cleared successfully for userId={}", user.getId());
        return ResponseEntity.noContent().build();
    }
}
package com.naman.capstone.controller;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.UpdateCartItemRequestDTO;
import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.service.CartItemService;
import com.naman.capstone.service.CurrentUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.naman.capstone.constant.AppConstants.CART_ITEM_ID;

/**
 * Handles operations related to cart items
 */
@RestController
@RequestMapping(AppConstants.CART_ITEM_BASE_URL)
public class CartItemController {

    private final CartItemService cartItemService;
    private final CurrentUserService currentUserService;

    public CartItemController(CartItemService cartItemService, CurrentUserService currentUserService) {
        this.cartItemService = cartItemService;
        this.currentUserService = currentUserService;
    }

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    /**
     * Updates the quantity of a specific cart item
     * @param cartItemId id of cart item
     * @param requestDTO request containing new quantity
     * @return updated cart response
     */
    @PutMapping(CART_ITEM_ID)
    public ResponseEntity<CartResponseDTO> updateQuantity(@PathVariable Long cartItemId,
                                                          @RequestBody @Valid UpdateCartItemRequestDTO requestDTO,
                                                          @AuthenticationPrincipal UserDetails userDetails) {

        User user = currentUserService.getCurrentUser(userDetails);

        log.info("Request received to update quantity for carItemId={}", cartItemId);
        CartResponseDTO response = cartItemService.updateQuantity(user, cartItemId, requestDTO.getQuantity());

        log.info("Quantity updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
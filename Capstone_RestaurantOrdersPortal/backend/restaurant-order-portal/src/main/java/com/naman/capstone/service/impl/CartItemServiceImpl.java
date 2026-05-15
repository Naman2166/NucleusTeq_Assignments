package com.naman.capstone.service.impl;

import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.Cart;
import com.naman.capstone.entity.CartItem;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.InvalidCartItemQuantityException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.mapper.CartMapper;
import com.naman.capstone.repository.CartItemRepository;
import com.naman.capstone.repository.CartRepository;
import com.naman.capstone.service.CartItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * service implementation for managing cart items
 */
@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartRepository cartRepository,
                               CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * update quantity of cart item
     * @param user user object
     * @param cartItemId id of cart item
     * @param quantity new quantity
     * @return updated cart data
     */
    @Override
    public CartResponseDTO updateQuantity(User user, Long cartItemId, int quantity) {

        logger.info("updating quantity for cartItemId {} for user id {}", cartItemId, user.getId());

        if (quantity <= 0) {
            logger.error("invalid quantity for cartItemId {}", cartItemId);
            throw new InvalidCartItemQuantityException("Quantity must be greater than 0");
        }

        // Get cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("cart not found for user id {}", user.getId());
                    return new ResourceNotFoundException("Cart not found");
                });

        // Get item
        CartItem item = cartItemRepository.findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> {
                    logger.error("cart item not found for id {}", cartItemId);
                    return new ResourceNotFoundException("Cart item not found");
                });

        // Update quantity and total price
        item.setQuantity(quantity);
        item.calculateTotalPrice();

        //calculate cart total
        calculateCartTotal(cart);

        cartRepository.save(cart);
        logger.info("quantity updated successfully for cartItemId {}", cartItemId);

        return CartMapper.toDTO(cart);
    }


    /**
     * calculate total price of cart
     * @param cart cart whose total need to find
     */
    //helper method
    public void calculateCartTotal(Cart cart) {

        logger.debug("calculating total for cart id {}", cart.getId());
        List<CartItem> items = cart.getItems();

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getTotalPrice());
        }
        cart.setTotalPrice(total);
        logger.debug("cart total updated to {}", total);
    }
}
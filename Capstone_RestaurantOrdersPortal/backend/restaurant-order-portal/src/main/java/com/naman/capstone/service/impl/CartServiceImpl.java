package com.naman.capstone.service.impl;

import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.*;
import com.naman.capstone.exception.InvalidCartItemQuantityException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.mapper.CartMapper;
import com.naman.capstone.repository.*;
import com.naman.capstone.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * service implementation for managing cart
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * add item to cart
     * @param user logged-in user
     * @param menuItemId id of menu item
     * @param quantity quantity to add
     * @return updated cart data
     */
    @Override
    public CartResponseDTO addItem(User user, Long menuItemId, int quantity) {

        logger.info("adding item with id {} to cart for user id {}", menuItemId, user.getId());

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> {
                    logger.error("menu item not found for id {}", menuItemId);
                    return new ResourceNotFoundException("Menu item not found");
                });

        CartItem existingCartItem = cartItemRepository.findByCartAndMenuItem(cart, menuItem).orElse(null);

        if (quantity <= 0) {
            logger.error("invalid quantity for menuItemId {}", menuItemId);
            throw new InvalidCartItemQuantityException("Quantity must be greater than 0");
        }

        //if item already exist in cart and not empty then update it otherwise create new item
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.calculateTotalPrice();
        } else {
            BigDecimal unitPrice = menuItem.getPrice();
            CartItem newItem = new CartItem(cart, menuItem, quantity, unitPrice);
            newItem.calculateTotalPrice();
            cart.addItem(newItem);
        }

        //Update cart
        calculateCartTotal(cart);
        cartRepository.save(cart);

        logger.info("item added successfully for menuItemId {}", menuItemId);

        return CartMapper.toDTO(cart);
    }


    /**
     * get cart of user
     * @param user logged-in user
     * @return cart data
     */
    @Override
    public CartResponseDTO getCart(User user) {

        logger.info("fetching cart for user id {}", user.getId());

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return CartMapper.toDTO(cart);
    }


    /**
     * remove item from cart
     * @param user logged-in user
     * @param cartItemId id of cart item
     */
    @Override
    public void removeItem(User user, Long cartItemId) {

        logger.info("removing cart item with id {} for user id {}", cartItemId, user.getId());

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cartItemRepository.findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cart.removeItem(item);
        calculateCartTotal(cart);

        cartRepository.save(cart);

        logger.info("cart item removed successfully for id {}", cartItemId);
    }


    /**
     * clear cart of user
     * @param user logged-in user
     */
    @Override
    public void clearCart(User user) {

        logger.info("clearing cart for user id {}", user.getId());

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("cart not found for user id {}", user.getId());
                    return new ResourceNotFoundException("Cart not found");
                });

        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);

        cartRepository.save(cart);

        logger.info("cart cleared successfully for user id {}", user.getId());
    }


    /**
     * calculate total price of cart
     * @param cart the cart
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
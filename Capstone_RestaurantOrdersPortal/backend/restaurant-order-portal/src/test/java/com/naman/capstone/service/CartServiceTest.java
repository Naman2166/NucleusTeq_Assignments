package com.naman.capstone.service;

import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.*;
import com.naman.capstone.exception.InvalidCartItemQuantityException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.repository.CartItemRepository;
import com.naman.capstone.repository.CartRepository;
import com.naman.capstone.repository.MenuItemRepository;
import com.naman.capstone.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    MenuItemRepository menuItemRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @InjectMocks
    CartServiceImpl cartService;

    /**
     * Test adding item to cart
     */
    @Test
    void add_item_success() {
        User user = new User();
        user.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(10L);
        menuItem.setPrice(new BigDecimal("200"));
        menuItem.setRestaurant(restaurant);

        Cart cart = new Cart(user);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        when(menuItemRepository.findById(10L)).thenReturn(Optional.of(menuItem));

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        when(cartItemRepository.findByCartAndMenuItem(cart, menuItem)).thenReturn(Optional.empty());

        CartResponseDTO response = cartService.addItem(user, 10L, 2);
        assertNotNull(response);
    }

    /**
     * Test menu item not found
     */
    @Test
    void add_item_not_found() {
        User user = new User();
        user.setId(1L);

        when(menuItemRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.addItem(user, 10L, 1)); // ✅ FIX
    }


    /**
     * Test get cart
     */
    @Test
    void get_cart_success() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart(user);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        CartResponseDTO response = cartService.getCart(user);
        assertNotNull(response);
    }


    /**
     * Test cart not found
     */
    @Test
    void get_cart_not_found() {
        User user = new User();
        user.setId(1L);

        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.getCart(user));
    }


    /**
     * Test clear cart
     */
    @Test
    void clear_cart_success() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.getItems().add(new CartItem());

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        cartService.clearCart(user);
        assertTrue(cart.getItems().isEmpty());
    }


    /**
     * testing add item when item already exists in cart
     */
    @Test
    void add_item_existing_item() {

        User user = new User();
        user.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setPrice(new BigDecimal("100"));

        CartItem existingItem = new CartItem();
        existingItem.setMenuItem(menuItem);
        existingItem.setUnitPrice(new BigDecimal("100"));
        existingItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(List.of(existingItem)));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(cartItemRepository.findByCartAndMenuItem(cart, menuItem)).thenReturn(Optional.of(existingItem));
        cartService.addItem(user, 1L, 3);

        assertEquals(5, existingItem.getQuantity());
    }


    /**
     * testing add item with invalid quantity
     */
    @Test
    void add_item_invalid_quantity() {

        User user = new User();
        user.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setPrice(new BigDecimal("100"));

        Cart cart = new Cart(user);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(cartItemRepository.findByCartAndMenuItem(cart, menuItem)).thenReturn(Optional.empty());

        assertThrows(InvalidCartItemQuantityException.class,
                () -> cartService.addItem(user, 1L, 0));
    }


    /**
     * testing add item when cart does not exist
     */
    @Test
    void add_item_new_cart_created() {

        User user = new User();
        user.setId(1L);

        Restaurant restaurant = new Restaurant();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setPrice(new BigDecimal("200"));
        menuItem.setRestaurant(restaurant);

        Cart newCart = new Cart(user);

        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        when(cartItemRepository.findByCartAndMenuItem(any(), eq(menuItem))).thenReturn(Optional.empty());

        CartResponseDTO response = cartService.addItem(user, 1L, 2);

        assertNotNull(response);
    }


    /**
     * testing remove item success
     */
    @Test
    void remove_item_success() {

        User user = new User();
        user.setId(1L);

        Cart cart = new Cart(user);

        CartItem item = new CartItem();
        item.setId(1L);

        cart.setItems(new ArrayList<>(List.of(item)));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndCart(1L, cart)).thenReturn(Optional.of(item));

        assertDoesNotThrow(() -> cartService.removeItem(user, 1L));

        verify(cartRepository).save(cart);
    }


    /**
     * testing remove item when cart not found
     */
    @Test
    void remove_item_cart_not_found() {

        User user = new User();
        user.setId(1L);

        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.removeItem(user, 1L));
    }


    /**
     * testing remove item when cart item not found
     */
    @Test
    void remove_item_not_found() {

        User user = new User();
        user.setId(1L);

        Cart cart = new Cart(user);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        when(cartItemRepository.findByIdAndCart(1L, cart)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.removeItem(user, 1L));
    }


    /**
     * testing clear cart when cart not found
     */
    @Test
    void clear_cart_not_found() {

        User user = new User();
        user.setId(1L);

        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartService.clearCart(user));
    }


    /**
     * testing add item when menu item not found
     */
    @Test
    void add_item_menu_not_found() {
        User user = new User();
        user.setId(1L);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                cartService.addItem(user, 1L, 2));
    }


}
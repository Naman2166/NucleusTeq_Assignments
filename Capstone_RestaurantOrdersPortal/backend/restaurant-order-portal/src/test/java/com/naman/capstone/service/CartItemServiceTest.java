package com.naman.capstone.service;

import com.naman.capstone.dto.response.CartResponseDTO;
import com.naman.capstone.entity.Cart;
import com.naman.capstone.entity.CartItem;
import com.naman.capstone.entity.MenuItem;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.InvalidCartItemQuantityException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.mapper.CartMapper;
import com.naman.capstone.repository.CartItemRepository;
import com.naman.capstone.repository.CartRepository;
import com.naman.capstone.service.impl.CartItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * cart item service impl test
 */
@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;


    /**
     * testing update quantity success functionality
     */
    @Test
    void test_update_quantity_success() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(10L);

        CartItem item = new CartItem();
        item.setMenuItem(menuItem);
        item.setUnitPrice(new BigDecimal("50"));
        item.setQuantity(2);

        cart.setItems(List.of(item));

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndCart(1L, cart)).thenReturn(Optional.of(item));

        CartResponseDTO response = cartItemService.updateQuantity(user, 1L, 3);

        assertNotNull(response);
        assertEquals(new BigDecimal("150"), cart.getTotalPrice());
        verify(cartRepository).save(cart);
    }


    /**
     * testing invalid quantity
     */
    @Test
    void test_invalid_quantity() {
        User user = new User();
        user.setId(1L);
        assertThrows(InvalidCartItemQuantityException.class, () -> cartItemService.updateQuantity(user, 1L, 0));
    }


    /**
     * testing cart not found
     */
    @Test
    void test_cart_not_found() {
        User user = new User();
        user.setId(1L);
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartItemService.updateQuantity(user, 1L, 2));
    }


    /**
     * testing cart item not found
     */
    @Test
    void test_item_not_found() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndCart(1L, cart)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cartItemService.updateQuantity(user, 1L, 2));
    }


    /**
     * testing cart total amount calculational method
     */
    @Test
    void test_calculate_cart_total() {
        CartItem item1 = new CartItem();
        item1.setUnitPrice(new BigDecimal("50"));
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setUnitPrice(new BigDecimal("100"));
        item2.setQuantity(1);

        Cart cart = new Cart();
        cart.setItems(List.of(item1, item2));

        cartItemService.calculateCartTotal(cart);
        assertEquals(new BigDecimal("200"), cart.getTotalPrice());
    }
}
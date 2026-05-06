package com.naman.capstone.service;

import com.naman.capstone.dto.request.MenuItemRequestDTO;
import com.naman.capstone.dto.response.MenuItemResponseDTO;
import com.naman.capstone.entity.*;
import com.naman.capstone.exception.CategoryRestaurantMismatchException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.exception.UnauthorizedException;
import com.naman.capstone.repository.CategoryRepository;
import com.naman.capstone.repository.MenuItemRepository;
import com.naman.capstone.repository.RestaurantRepository;
import com.naman.capstone.service.impl.MenuItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MenuItemService.
 */
@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    MenuItemRepository menuItemRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    RestaurantRepository restaurantRepository;

    @InjectMocks
    MenuItemServiceImpl menuItemService;

    /**
     * Test create menu item success
     */
    @Test
    void create_menu_item_success() {

        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        Category category = new Category();
        category.setId(1L);
        category.setRestaurant(restaurant);

        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setName("Pizza");
        request.setPrice(new BigDecimal("200"));
        request.setRestaurantId(1L);
        request.setCategoryId(1L);

        MenuItem saved = new MenuItem();
        saved.setId(10L);
        saved.setRestaurant(restaurant);
        saved.setCategory(category);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(saved);

        MenuItemResponseDTO res = menuItemService.createMenuItem(request, owner);

        assertNotNull(res);
    }

    /**
     * Test unauthorized create
     */
    @Test
    void create_menu_item_unauthorized() {

        User owner = new User(); owner.setId(1L);
        User other = new User(); other.setId(2L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        Category category = new Category();
        category.setRestaurant(restaurant);

        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setRestaurantId(1L);
        request.setCategoryId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(UnauthorizedException.class, () -> menuItemService.createMenuItem(request, other));
    }

    /**
     * Test category mismatch
     */
    @Test
    void create_menu_item_category_mismatch() {

        User owner = new User(); owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        Restaurant otherRestaurant = new Restaurant();
        otherRestaurant.setId(2L);

        Category category = new Category();
        category.setRestaurant(otherRestaurant);

        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setRestaurantId(1L);
        request.setCategoryId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(CategoryRestaurantMismatchException.class, () -> menuItemService.createMenuItem(request, owner));
    }

    /**
     * Test update menu item
     */
    @Test
    void update_menu_item_success() {

        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        Category category = new Category();
        category.setId(1L);
        category.setRestaurant(restaurant);

        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setRestaurant(restaurant);

        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setName("Updated");
        request.setPrice(new BigDecimal("300"));
        request.setCategoryId(1L);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(item);

        MenuItemResponseDTO res =
                menuItemService.updateMenuItem(1L, request, owner);

        assertNotNull(res);
    }

    /**
     * Test delete unauthorized
     */
    @Test
    void delete_menu_item_unauthorized() {

        User owner = new User(); owner.setId(1L);
        User other = new User(); other.setId(2L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        MenuItem item = new MenuItem();
        item.setRestaurant(restaurant);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(UnauthorizedException.class,
                () -> menuItemService.deleteMenuItem(1L, other));
    }

    /**
     * Test get menu items by restaurant
     */
    @Test
    void get_menu_items_by_restaurant_success() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setRestaurant(restaurant);

        MenuItem item = new MenuItem();
        item.setId(10L);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurantId(1L)).thenReturn(List.of(item));

        List<MenuItemResponseDTO> list =
                menuItemService.getMenuItemsByRestaurant(1L);

        assertEquals(1, list.size());
    }

    /**
     * Test get menu items by category
     */
    @Test
    void get_menu_items_by_category_success() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setRestaurant(restaurant);

        MenuItem item = new MenuItem();
        item.setId(10L);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(menuItemRepository.findByCategoryId(1L)).thenReturn(List.of(item));

        List<MenuItemResponseDTO> list =
                menuItemService.getMenuItemsByCategory(1L);

        assertEquals(1, list.size());
    }


    /**
     * test create menu item when restaurant not found
     */
    @Test
    void create_menu_item_restaurant_not_found() {

        User owner = new User();
        owner.setId(1L);

        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setRestaurantId(1L);
        request.setCategoryId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.createMenuItem(request, owner));
    }


    /**
     * test create menu item when category not found
     */
    @Test
    void create_menu_item_category_not_found() {

        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setRestaurantId(1L);
        request.setCategoryId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.createMenuItem(request, owner));
    }


    /**
     * test update menu item when item not found
     */
    @Test
    void update_menu_item_not_found() {

        User owner = new User();
        owner.setId(1L);

        MenuItemRequestDTO request = new MenuItemRequestDTO();
        request.setCategoryId(1L);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.updateMenuItem(1L, request, owner));
    }


    /**
     * test delete menu item success
     */
    @Test
    void delete_menu_item_success() {

        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        MenuItem item = new MenuItem();
        item.setRestaurant(restaurant);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertDoesNotThrow(() -> menuItemService.deleteMenuItem(1L, owner));
        verify(menuItemRepository).delete(item);
    }


    /**
     * testing delete item when item not found
     */
    @Test
    void delete_menu_item_not_found() {

        User owner = new User();
        owner.setId(1L);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.deleteMenuItem(1L, owner));
    }


    /**
     * test get menu items by restaurant when restaurant not found
     */
    @Test
    void get_menu_items_by_restaurant_not_found() {

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> menuItemService.getMenuItemsByRestaurant(1L));
    }


    /**
     * test get menu items by category when category not found
     */
    @Test
    void get_menu_items_by_category_not_found() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> menuItemService.getMenuItemsByCategory(1L));
    }

}
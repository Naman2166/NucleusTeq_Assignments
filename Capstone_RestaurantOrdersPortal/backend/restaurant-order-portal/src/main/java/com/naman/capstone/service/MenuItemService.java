package com.naman.capstone.service;

import com.naman.capstone.dto.request.MenuItemRequestDTO;
import com.naman.capstone.dto.response.MenuItemResponseDTO;
import com.naman.capstone.entity.User;

import java.util.List;

/**
 * Defines methods for menu item operations
 */
public interface MenuItemService {

    /**
     * Create a new menu item
     * @param request data for adding a new menu item
     * @param user authenticated user
     * @return created menu item details
     */
    MenuItemResponseDTO createMenuItem(MenuItemRequestDTO request, User user);

    /**
     * Update existing menu item
     * @param id menu item id
     * @param request contains details for updating a menu item
     * @param user authenticated user
     * @return updated menu item
     */
    MenuItemResponseDTO updateMenuItem(Long id, MenuItemRequestDTO request, User user);

    /**
     * Delete a menu item
     * @param id menu item id
     * @param user authenticated user
     */
    void deleteMenuItem(Long id, User user);

    /**
     * get all menu items of a specific restaurant
     * @param restaurantId restaurant id
     * @return list of all menu items of that restaurant
     */
    List<MenuItemResponseDTO> getMenuItemsByRestaurant(Long restaurantId);

    /**
     * get all menu items of a specific category
     * @param categoryId category id
     * @return list of menu items of that category
     */
    List<MenuItemResponseDTO> getMenuItemsByCategory(Long categoryId);
}
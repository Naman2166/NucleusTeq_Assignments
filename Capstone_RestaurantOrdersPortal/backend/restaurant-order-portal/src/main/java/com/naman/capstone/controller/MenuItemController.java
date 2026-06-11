package com.naman.capstone.controller;

import com.naman.capstone.constant.AppConstants;
import com.naman.capstone.dto.request.MenuItemRequestDTO;
import com.naman.capstone.dto.response.MenuItemResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.service.CurrentUserService;
import com.naman.capstone.service.MenuItemService;
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
 * This class handles all API request related to menu item inside a category
 */
@RestController
@RequestMapping(AppConstants.MENU_ITEM_BASE_URL)
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final CurrentUserService currentUserService;

    public MenuItemController(MenuItemService menuItemService, CurrentUserService currentUserService) {
        this.menuItemService = menuItemService;
        this.currentUserService = currentUserService;
    }

    private static final Logger log = LoggerFactory.getLogger(MenuItemController.class);

    /**
     * Creates a new menu item
     * @param request menu item details
     * @param userDetails authenticated user
     * @return created menu item
     */
    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(@Valid @RequestBody MenuItemRequestDTO request,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Create menu item request received");
        User user = currentUserService.getCurrentUser(userDetails);
        MenuItemResponseDTO response = menuItemService.createMenuItem(request, user);
        log.info("Menu item created successfully by userId={}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * update existing menu item
     * @param id menu item id
     * @param request updated menu item details
     * @param userDetails authenticated user
     * @return updated menu item
     */
    @PutMapping(MENU_ITEM_ID)
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(@PathVariable("menuItemId") Long id,
                                                              @Valid @RequestBody MenuItemRequestDTO request,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Update menu item request received for menuItemId={}", id);
        User user = currentUserService.getCurrentUser(userDetails);
        MenuItemResponseDTO response = menuItemService.updateMenuItem(id, request, user);
        log.info("Menu item updated successfully for menuItemId={}", id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * delete a menu item
     * @param id menu item id
     * @param userDetails authenticated user
     * @return item deleted message
     */
    @DeleteMapping(MENU_ITEM_ID)
    public ResponseEntity<String> deleteMenuItem(@PathVariable("menuItemId") Long id,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.warn("Delete menu item request received for menuItemId={}", id);
        User user = currentUserService.getCurrentUser(userDetails);
        menuItemService.deleteMenuItem(id, user);
        log.info("Menu item deleted successfully with id={}", id);

        return ResponseEntity.status(HttpStatus.OK).body("Menu item deleted successfully with id: " + id);
    }


    /**
     * get all menu items of a restaurant
     * @param restaurantId restaurant id
     * @return list of menu items of a restaurant
     */
    @GetMapping(RESTAURANT + RESTAURANT_ID)
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        log.info("Fetching menu items for restaurantId={}", restaurantId);
        List<MenuItemResponseDTO> response = menuItemService.getMenuItemsByRestaurant(restaurantId);
        log.info("Fetched menu items for restaurantId={}", restaurantId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * gets all menu items of a category
     * @param categoryId category ID
     * @return list of menu item of a category
     */
    @GetMapping(CATEGORY + CATEGORY_ID)
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        log.info("Fetching menu items for categoryId={}", categoryId);
        List<MenuItemResponseDTO> response = menuItemService.getMenuItemsByCategory(categoryId);
        log.info("Fetched menu items for categoryId={}", categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
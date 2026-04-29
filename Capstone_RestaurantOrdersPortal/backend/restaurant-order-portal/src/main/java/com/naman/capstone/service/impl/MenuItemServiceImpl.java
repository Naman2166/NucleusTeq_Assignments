package com.naman.capstone.service.impl;

import com.naman.capstone.dto.request.MenuItemRequestDTO;
import com.naman.capstone.dto.response.MenuItemResponseDTO;
import com.naman.capstone.entity.Category;
import com.naman.capstone.entity.MenuItem;
import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.*;
import com.naman.capstone.mapper.MenuItemMapper;
import com.naman.capstone.repository.CategoryRepository;
import com.naman.capstone.repository.MenuItemRepository;
import com.naman.capstone.repository.RestaurantRepository;
import com.naman.capstone.service.MenuItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * service implementation for managing menu items
 */
@Service
public class MenuItemServiceImpl implements MenuItemService {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * create menu item
     * @param requestDTO menu item data
     * @param user the user
     * @return created menu item
     */
    @Override
    public MenuItemResponseDTO createMenuItem(MenuItemRequestDTO requestDTO, User user) {

        logger.info("creating menu item {} for restaurant id {}", requestDTO.getName(), requestDTO.getRestaurantId());

        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with restaurantId: " + requestDTO.getRestaurantId()));

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        //checking owner
        if (!restaurant.getOwner().getId().equals(user.getId())) {
            logger.error("unauthorized access by user id {}", user.getId());
            throw new UnauthorizedException("You are not allowed to add items to this restaurant");
        }

        //checking whether category belongs to same restaurant or not
        if (!category.getRestaurant().getId().equals(restaurant.getId())) {
            throw new CategoryRestaurantMismatchException("Category does not belong to this restaurant");
        }

        MenuItem item = new MenuItem(requestDTO.getName(), requestDTO.getPrice(), category, restaurant);
        MenuItem savedItem = menuItemRepository.save(item);
        logger.info("menu item created with id {}", savedItem.getId());

        return MenuItemMapper.toDTO(savedItem);
    }


    /**
     * update menu item
     * @param id menu item id
     * @param requestDTO updated data
     * @param user the user
     * @return updated menu item
     */
    @Override
    public MenuItemResponseDTO updateMenuItem(Long id, MenuItemRequestDTO requestDTO, User user) {

        logger.info("updating menu item id {}", id);

        //checking item in menu
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        Restaurant restaurant = item.getRestaurant();

        //checking category
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getRestaurant().getId().equals(restaurant.getId())) {
            logger.error("category restaurant mismatch for category id {}", requestDTO.getCategoryId());
            throw new CategoryRestaurantMismatchException("Category does not belong to this restaurant");
        }

        //checking owner
        if (!restaurant.getOwner().getId().equals(user.getId())) {
            logger.error("unauthorized update attempt by user id {}", user.getId());
            throw new UnauthorizedException("You are not allowed to update this item");
        }

        //updating item information
        item.setName(requestDTO.getName());
        item.setPrice(requestDTO.getPrice());
        item.setCategory(category);

        MenuItem updatedItem = menuItemRepository.save(item);
        logger.info("menu item updated successfully for id {}", id);

        return MenuItemMapper.toDTO(updatedItem);
    }


    /**
     * delete menu item
     * @param id menu item id
     * @param user the user
     */
    @Override
    public void deleteMenuItem(Long id, User user) {

        logger.info("deleting menu item id {}", id);
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        Restaurant restaurant = item.getRestaurant();

        //checking owner
        if (!restaurant.getOwner().getId().equals(user.getId())) {
            logger.error("unauthorized delete attempt by user id {}", user.getId());
            throw new UnauthorizedException("You are not allowed to delete this item");
        }

        menuItemRepository.delete(item);
        logger.info("menu item deleted successfully for id {}", id);
    }


    /**
     * get menu items by restaurant
     * @param restaurantId id of restaurant
     * @return list of menu items
     */
    @Override
    public List<MenuItemResponseDTO> getMenuItemsByRestaurant(Long restaurantId) {

        logger.info("fetching menu items for restaurant id {}", restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with restaurantId: " + restaurantId));

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(item -> MenuItemMapper.toDTO(item))
                .collect(Collectors.toList());
    }


    /**
     * get menu items by category
     * @param categoryId id of category
     * @return list of menu items
     */
    @Override
    public List<MenuItemResponseDTO> getMenuItemsByCategory(Long categoryId) {

        logger.info("fetching menu items for category id {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with categoryId: " + categoryId));

        return menuItemRepository.findByCategoryId(categoryId)
                .stream()
                .map(item -> MenuItemMapper.toDTO(item))
                .collect(Collectors.toList());
    }
}
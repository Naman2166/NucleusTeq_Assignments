package com.naman.capstone.repository;

import com.naman.capstone.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database operations for menu items
 */
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * get all menu items of a restaurant
     * @param restaurantId id of restaurant
     * @return list of menu items
     */
    List<MenuItem> findByRestaurantId(Long restaurantId);

    /**
     * get all menu items of a category
     * @param categoryId id of category
     * @return list of menu items
     */
    List<MenuItem> findByCategoryId(Long categoryId);
}
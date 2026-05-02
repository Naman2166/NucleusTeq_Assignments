package com.naman.capstone.repository;

import com.naman.capstone.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database operations for category entities
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * find all categories of a restaurant
     * @param restaurantId id of restaurant
     * @return list of categories
     */
    List<Category> findByRestaurantId(Long restaurantId);

    /**
     * check category exists or not in restaurant
     * @param name category name
     * @param restaurantId id of restaurant
     * @return true if exists else false
     */
    boolean existsByNameIgnoreCaseAndRestaurantId(String name, Long restaurantId);
}
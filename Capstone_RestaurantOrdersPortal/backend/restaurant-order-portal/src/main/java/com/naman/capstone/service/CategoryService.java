package com.naman.capstone.service;

import com.naman.capstone.dto.request.CategoryRequestDTO;
import com.naman.capstone.dto.response.CategoryResponseDTO;
import com.naman.capstone.entity.User;

import java.util.List;

/**
 * this interface handles category related operations
 */
public interface CategoryService {

    /**
     * creates a new category for a specific restaurant
     * @param requestDTO contains category details
     * @param user current authenticated user
     * @return created category details
     */
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO, User user);

    /**
     * Update an existing category
     * @param id category id
     * @param requestDTO contains updated category details
     * @param user current authenticated user
     * @return updated category details
     */
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO, User user);

    /**
     * Delete a category
     * @param id category id
     * @param user current authenticated user
     */
    void deleteCategory(Long id, User user);

    /**
     * get all categories of a specific restaurant
     * @param restaurantId restaurant id
     * @return list of categories of that restaurant
     */
    List<CategoryResponseDTO> getCategoriesByRestaurant(Long restaurantId);
}
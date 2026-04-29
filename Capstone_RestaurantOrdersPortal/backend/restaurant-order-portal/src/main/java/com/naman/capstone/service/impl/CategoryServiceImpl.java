package com.naman.capstone.service.impl;

import com.naman.capstone.dto.request.CategoryRequestDTO;
import com.naman.capstone.dto.response.CategoryResponseDTO;
import com.naman.capstone.entity.Category;
import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.CategoryAlreadyExistsException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.exception.UnauthorizedException;
import com.naman.capstone.mapper.CategoryMapper;
import com.naman.capstone.repository.CategoryRepository;
import com.naman.capstone.repository.RestaurantRepository;
import com.naman.capstone.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * this class contains operations for managing category
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, RestaurantRepository restaurantRepository) {
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }


    /**
     * create new category
     * @param requestDTO category data
     * @param user the user
     * @return created category
     */
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO, User user) {

        logger.info("creating category {} for restaurant id {}", requestDTO.getName(), requestDTO.getRestaurantId());

        //checking restaurant
        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> {
                    logger.error("restaurant not found with id {}", requestDTO.getRestaurantId());
                    return new ResourceNotFoundException("Restaurant not found with id: " + requestDTO.getRestaurantId());
                });

        //checking category already exists or not
        if (categoryRepository.existsByNameIgnoreCaseAndRestaurantId(requestDTO.getName(), requestDTO.getRestaurantId())) {
            logger.error("category already exists: {}", requestDTO.getName());
            throw new CategoryAlreadyExistsException("Category already exists for this restaurant");
        }

        //checking owner
        if (!restaurant.getOwner().getId().equals(user.getId())) {
            logger.error("unauthorized access by user id {}", user.getId());
            throw new UnauthorizedException("You are not allowed to add category to this restaurant");
        }

        Category category = new Category(requestDTO.getName(), restaurant);
        Category savedCategory = categoryRepository.save(category);
        logger.info("category created with id {}", savedCategory.getId());

        return CategoryMapper.toDTO(savedCategory);
    }


    /**
     * update category
     * @param id category id
     * @param requestDTO updated data
     * @param user the user
     * @return updated category
     */
    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO, User user) {

        logger.info("updating category id {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("category not found with id {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });

        Restaurant restaurant = category.getRestaurant();

        //checking owner
        if (!restaurant.getOwner().getId().equals(user.getId())) {
            logger.error("unauthorized update attempt by user id {}", user.getId());
            throw new UnauthorizedException("You are not allowed to update this category");
        }

        category.setName(requestDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        logger.info("category updated successfully for id {}", id);

        return CategoryMapper.toDTO(updatedCategory);
    }


    /**
     * delete category
     * @param id category id
     * @param user the user
     */
    @Override
    public void deleteCategory(Long id, User user) {

        logger.info("deleting category id {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        Restaurant restaurant = category.getRestaurant();

        //checking owner
        if (!restaurant.getOwner().getId().equals(user.getId())) {
            logger.error("unauthorized delete attempt by user id {}", user.getId());
            throw new UnauthorizedException("You are not allowed to delete this category");
        }

        categoryRepository.delete(category);
        logger.info("category deleted successfully for id {}", id);
    }


    /**
     * get categories by restaurant
     * @param restaurantId id of restaurant
     * @return list of categories
     */
    @Override
    public List<CategoryResponseDTO> getCategoriesByRestaurant(Long restaurantId) {

        logger.info("fetching categories for restaurant id {}", restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with this ID"));

        return categoryRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(category -> CategoryMapper.toDTO(category))
                .collect(Collectors.toList());
    }
}
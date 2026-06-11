package com.naman.capstone.controller;

import com.naman.capstone.dto.request.CategoryRequestDTO;
import com.naman.capstone.dto.response.CategoryResponseDTO;
import com.naman.capstone.entity.User;
import com.naman.capstone.service.CategoryService;
import com.naman.capstone.service.CurrentUserService;
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
 * This class handles all API request related to categories of a restaurant
 */
@RestController
@RequestMapping(CATEGORY_BASE_URL)
public class CategoryController {

    private final CategoryService categoryService;
    private final CurrentUserService currentUserService;

    public CategoryController(CategoryService categoryService, CurrentUserService currentUserService) {
        this.categoryService = categoryService;
        this.currentUserService = currentUserService;
    }

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);


    /**
     * Creates a new category
     * @param requestDTO data for creating a category
     * @param userDetails current authenticated user
     * @return created category details
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Create category request received");
        User user = currentUserService.getCurrentUser(userDetails);
        CategoryResponseDTO response = categoryService.createCategory(requestDTO, user);
        log.info("Category created successfully by userId={}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Updates an existing category by id
     * @param id category id
     * @param requestDTO updated category data
     * @param userDetails current authenticated user
     * @return updated category data
     */
    @PutMapping(CATEGORY_ID)
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable("categoryId") Long id,
                                              @Valid @RequestBody CategoryRequestDTO requestDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Update category request received for categoryId={}", id);
        User user = currentUserService.getCurrentUser(userDetails);
        CategoryResponseDTO response = categoryService.updateCategory(id, requestDTO, user);
        log.info("Category updated successfully for categoryId={}", id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * deletes a category by id
     * @param id category id which need to delete
     * @param userDetails current authenticated user
     * @return category deleted message
     */
    @DeleteMapping(CATEGORY_ID)
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long id, @AuthenticationPrincipal UserDetails userDetails) {

        log.warn("Delete category request received for categoryId={}", id);
        User user = currentUserService.getCurrentUser(userDetails);
        categoryService.deleteCategory(id, user);
        log.info("Category deleted successfully for categoryId={}", id);

        return ResponseEntity.status(HttpStatus.OK).body("category deleted successfully");
    }


    /** get all categories of a restaurant
     * @param restaurantId id of restaurant
     * @return list of all categories of a restaurant
     */
    @GetMapping(RESTAURANT + RESTAURANT_ID)
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByRestaurant(@PathVariable Long restaurantId) {

        log.info("Fetching categories for restaurantId={}", restaurantId);
        List<CategoryResponseDTO> response = categoryService.getCategoriesByRestaurant(restaurantId);
        log.info("Fetched categories for restaurantId={}", restaurantId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
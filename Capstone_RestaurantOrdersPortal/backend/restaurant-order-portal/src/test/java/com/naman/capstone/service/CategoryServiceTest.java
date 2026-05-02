package com.naman.capstone.service;

import com.naman.capstone.dto.request.CategoryRequestDTO;
import com.naman.capstone.dto.response.CategoryResponseDTO;
import com.naman.capstone.entity.Category;
import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import com.naman.capstone.exception.CategoryAlreadyExistsException;
import com.naman.capstone.exception.ResourceNotFoundException;
import com.naman.capstone.exception.UnauthorizedException;
import com.naman.capstone.repository.CategoryRepository;
import com.naman.capstone.repository.RestaurantRepository;
import com.naman.capstone.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * tests category service functionality
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    /**
     * testing create category success
     */
    @Test
    void create_category_success() {
        User owner = new User();
        owner.setId(1L);

        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Pizza");
        request.setRestaurantId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        Category category = new Category();
        category.setName("Pizza");
        category.setRestaurant(restaurant);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.existsByNameIgnoreCaseAndRestaurantId("Pizza", restaurant.getId())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO response = categoryService.createCategory(request, owner);

        assertNotNull(response);
        assertEquals("Pizza", response.getName());
        verify(restaurantRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }


    /**
     * testing create category when restaurant not found
     */
    @Test
    void create_category_restaurant_not_found() {
        User owner = new User();
        owner.setId(1L);

        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Pizza");
        request.setRestaurantId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.createCategory(request, owner));
        verify(restaurantRepository).findById(1L);
        verify(categoryRepository, never()).save(any());
    }


    /**
     * testing create category when category already exists
     */
    @Test
    void create_category_already_exists() {
        User owner = new User();
        owner.setId(1L);

        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Pizza");
        request.setRestaurantId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.existsByNameIgnoreCaseAndRestaurantId("Pizza", 1L)).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createCategory(request, owner));
        verify(categoryRepository, never()).save(any());
    }


    /**
     * testing create category unauthorized user
     */
    @Test
    void create_category_unauthorized() {

        User owner = new User();
        owner.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Pizza");
        request.setRestaurantId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwner(owner);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.existsByNameIgnoreCaseAndRestaurantId("Pizza", 1L)).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> categoryService.createCategory(request, otherUser));
    }


    /**
     * testing create category with null name
     */
    @Test
    void create_category_invalid_name() {

        User owner = new User();
        owner.setId(1L);

        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName(null);
        requestDTO.setRestaurantId(1L);

        assertThrows(IllegalArgumentException.class, () ->
                categoryService.createCategory(requestDTO, owner));
    }


    /**
     * testing create category with empty name
     */
    @Test
    void create_category_invalid_name_empty() {
        User owner = new User();
        owner.setId(1L);

        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("   ");
        request.setRestaurantId(1L);

        assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(request, owner));
    }


    /**
     * testing update category success
     */
    @Test
    void update_category_success() {

        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        Category category = new Category();
        category.setId(1L);
        category.setRestaurant(restaurant);

        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setName("Updated");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO response = categoryService.updateCategory(1L, request, owner);

        assertNotNull(response);
        verify(categoryRepository).save(category);
    }


    /**
     * testing update category when category not found
     */
    @Test
    void update_category_not_found() {

        User user = new User();
        user.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryService.updateCategory(1L, new CategoryRequestDTO(), user));
    }


    /**
     * testing delete category success
     */
    @Test
    void delete_category_success() {

        User owner = new User();
        owner.setId(1L);

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(owner);

        Category category = new Category();
        category.setRestaurant(restaurant);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        categoryService.deleteCategory(1L, owner);
        verify(categoryRepository).delete(category);
    }


    /**
     * testing delete category not found
     */
    @Test
    void delete_category_not_found() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteCategory(1L, new User()));
    }


    /**
     * testing get categories by restaurant success
     */
    @Test
    void get_categories_by_restaurant_success() {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category category = new Category();
        category.setName("Pizza");
        category.setRestaurant(restaurant);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.findByRestaurantId(1L)).thenReturn(List.of(category));

        List<CategoryResponseDTO> result = categoryService.getCategoriesByRestaurant(1L);
        assertEquals(1, result.size());
    }


    /**
     * testing get categories when restaurant not found
     */
    @Test
    void get_categories_restaurant_not_found() {

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryService.getCategoriesByRestaurant(1L));
    }
}
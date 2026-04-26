package com.naman.capstone.repository;

import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database operations for restaurant entities
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    //find all restaurants of a specific owner
    List<Restaurant> findByOwner(User owner);
}
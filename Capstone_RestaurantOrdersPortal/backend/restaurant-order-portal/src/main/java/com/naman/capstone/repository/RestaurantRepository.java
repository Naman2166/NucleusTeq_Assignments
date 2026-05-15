package com.naman.capstone.repository;

import com.naman.capstone.entity.Restaurant;
import com.naman.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database operations for restaurant entities
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * get all restaurants of an owner
     * @param owner user whose restaurant need to fetch
     * @return list of restaurants
     */
    List<Restaurant> findByOwner(User owner);
}
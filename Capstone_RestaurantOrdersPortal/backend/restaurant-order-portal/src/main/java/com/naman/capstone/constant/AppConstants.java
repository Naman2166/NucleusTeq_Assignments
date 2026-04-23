package com.naman.capstone.constant;

import java.math.BigDecimal;

/**
 * Contains constants which is used across the application
 */
public class AppConstants {

    private AppConstants() {}

    //User API
    public static final String USER_BASE_URL = "/api/users";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";

    //Restaurant API
    public static final String RESTAURANT_BASE_URL = "/api/restaurants";

    //Default Values
    public static final BigDecimal DEFAULT_WALLET_BALANCE = new BigDecimal("1000");
}
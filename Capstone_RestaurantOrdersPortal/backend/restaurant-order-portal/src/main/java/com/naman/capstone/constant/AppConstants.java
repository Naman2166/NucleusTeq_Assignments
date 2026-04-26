package com.naman.capstone.constant;

import java.math.BigDecimal;

/**
 * Contains constants which is used across the application
 */
public class AppConstants {

    private AppConstants() {}

    //Base URL constants
    public static final String USER_BASE_URL = "/api/users";
    public static final String RESTAURANT_BASE_URL = "/api/restaurants";
    public static final String CATEGORY_BASE_URL = "/api/categories";
    public static final String MENU_ITEM_BASE_URL = "/api/menu-items";
    public static final String CART_BASE_URL = "/api/cart";
    public static final String ORDER_BASE_URL = "/api/orders";

    //other URL constants
    public static final String MY_RESTAURANTS = "/my-restaurants";

    //Auth constant
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";

    //Roles constant
    public static final String ROLE_OWNER = "RESTAURANT_OWNER";
    public static final String ROLE_USER = "USER";

    //Default Values
    public static final BigDecimal DEFAULT_WALLET_BALANCE = new BigDecimal("1000");

}
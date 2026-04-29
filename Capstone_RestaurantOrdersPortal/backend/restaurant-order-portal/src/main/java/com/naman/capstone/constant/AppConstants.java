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
    public static final String CART_ITEM_BASE_URL = "/api/cart-items";
    public static final String ORDER_BASE_URL = "/api/orders";
    public static final String ADDRESS_BASE_URL = "/api/addresses";

    //other URL constants
    public static final String MY_RESTAURANTS = "/my-restaurants";
    public static final String ADDRESS_ID = "/{addressId}";
    public static final String CART_ITEMS = "/items";
    public static final String CART_ITEM_ID = "/{cartItemId}";
    public static final String CATEGORY = "/category";
    public static final String CATEGORY_ID = "/{categoryId}";
    public static final String RESTAURANT = "/restaurants";
    public static final String RESTAURANT_ID = "/{restaurantID}";
    public static final String MENU_ITEM_ID = "/{menuItemId}";
    public static final String ORDER_ID = "/{orderId}";
    public static final String CANCEL = "/cancel";
    public static final String CANCEL_BY_OWNER = "/cancel-by-owner";
    public static final String STATUS = "/status";

    //Auth constant
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";

    //Roles constant
    public static final String ROLE_OWNER = "RESTAURANT_OWNER";
    public static final String ROLE_USER = "USER";

    //Default Values
    public static final BigDecimal DEFAULT_WALLET_BALANCE = new BigDecimal("1000");

}
package com.naman.capstone.dto.response;

import java.math.BigDecimal;

/**
 * dto representing menu item response data
 */
public class MenuItemResponseDTO {

    /**
     * id of menu item
     */
    private Long id;

    /**
     * name of menu item
     */
    private String name;

    /**
     * price of menu item
     */
    private BigDecimal price;

    /**
     * id of category to which item belongs
     */
    private Long categoryId;

    /**
     * id of restaurant to which item belongs
     */
    private Long restaurantId;

    /**
     * path of image
     */
    private String imageUrl;




    /**
     * default constructor
     */
    public MenuItemResponseDTO() {}

    /**
     * parameterized constructor
     */
    public MenuItemResponseDTO(Long id, String name, BigDecimal price, Long categoryId, Long restaurantId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.restaurantId = restaurantId;
        this.imageUrl = imageUrl;
    }



    /**
     * getters and setters for all fields
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }




    /**
     * returns string representation of object
     */
    @Override
    public String toString() {
        return "MenuItemResponseDTO{" +
                "id=" + id +
                ", name='" + name +
                "', price=" + price +
                ", categoryId=" + categoryId +
                ", restaurantId=" + restaurantId +
                "}";
    }

    /**
     * compares objects for equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MenuItemResponseDTO)) return false;
        MenuItemResponseDTO otherObj = (MenuItemResponseDTO) obj;
        return java.util.Objects.equals(id, otherObj.id) &&
                java.util.Objects.equals(name, otherObj.name) &&
                java.util.Objects.equals(price, otherObj.price) &&
                java.util.Objects.equals(categoryId, otherObj.categoryId) &&
                java.util.Objects.equals(restaurantId, otherObj.restaurantId);
    }

    /**
     * generates hash code
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, price, categoryId, restaurantId);
    }

}
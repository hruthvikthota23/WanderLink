package com.wanderlink.dto;

import com.wanderlink.entity.FoodListing;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class FoodListingDTO {

    private Long id;
    private Long providerId;
    private String providerName;
    private String providerAvatar;
    @NotBlank(message = "Food name is required")
    private String foodName;
    @NotBlank(message = "Cuisine is required")
    private String cuisine;
    @NotBlank(message = "Location is required")
    private String location;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    private String description;
    private String imageUrl;
    private String availableDays;
    private LocalDateTime createdAt;

    public FoodListingDTO() {
    }

    public static FoodListingDTO fromEntity(FoodListing food) {
        if (food == null) return null;
        FoodListingDTO dto = new FoodListingDTO();
        dto.setId(food.getId());
        if (food.getProvider() != null) {
            dto.setProviderId(food.getProvider().getId());
            dto.setProviderName(food.getProvider().getFullName());
            dto.setProviderAvatar(food.getProvider().getAvatarUrl());
        }
        dto.setFoodName(food.getFoodName());
        dto.setCuisine(food.getCuisine());
        dto.setLocation(food.getLocation());
        dto.setPrice(food.getPrice());
        dto.setDescription(food.getDescription());
        dto.setImageUrl(food.getImageUrl());
        dto.setAvailableDays(food.getAvailableDays());
        dto.setCreatedAt(food.getCreatedAt());
        return dto;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderAvatar() {
        return providerAvatar;
    }

    public void setProviderAvatar(String providerAvatar) {
        this.providerAvatar = providerAvatar;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

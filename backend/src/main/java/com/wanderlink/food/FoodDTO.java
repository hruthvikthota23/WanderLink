package com.wanderlink.food;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Objects for Food experiences.
 */
public class FoodDTO {

    /**
     * Request payload to publish a homemade food experience.
     */
    public static class CreateRequest {
        @NotNull(message = "Provider ID is required")
        private Long providerId;

        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title cannot exceed 150 characters")
        private String title;

        @NotBlank(message = "Cuisine is required")
        @Size(max = 100, message = "Cuisine cannot exceed 100 characters")
        private String cuisine;

        @NotBlank(message = "Location is required")
        @Size(max = 100, message = "Location cannot exceed 100 characters")
        private String location;

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        private Double price;

        private String description;
        private String imageUrl;

        public CreateRequest() {}

        public Long getProviderId() {
            return providerId;
        }

        public void setProviderId(Long providerId) {
            this.providerId = providerId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
    }

    /**
     * Response payload representing homemade food listing details.
     */
    public static class Response {
        private Long id;
        private Long providerId;
        private String providerName;
        private String title;
        private String cuisine;
        private String location;
        private Double price;
        private String description;
        private String imageUrl;

        public Response() {}

        public Response(Long id, Long providerId, String providerName, String title, String cuisine, String location, Double price, String description, String imageUrl) {
            this.id = id;
            this.providerId = providerId;
            this.providerName = providerName;
            this.title = title;
            this.cuisine = cuisine;
            this.location = location;
            this.price = price;
            this.description = description;
            this.imageUrl = imageUrl;
        }

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
    }
}

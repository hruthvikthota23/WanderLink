package com.wanderlink.accommodation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Objects for Accommodation listings.
 */
public class AccommodationDTO {

    /**
     * Request payload to publish an accommodation listing.
     */
    public static class CreateRequest {
        @NotNull(message = "Host ID is required")
        private Long hostId;

        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title cannot exceed 150 characters")
        private String title;

        @NotBlank(message = "Location is required")
        @Size(max = 100, message = "Location cannot exceed 100 characters")
        private String location;

        private String description;

        @NotNull(message = "Price per night is required")
        @Positive(message = "Price per night must be positive")
        private Double pricePerNight;

        private String imageUrl;

        public CreateRequest() {}

        public Long getHostId() {
            return hostId;
        }

        public void setHostId(Long hostId) {
            this.hostId = hostId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPricePerNight() {
            return pricePerNight;
        }

        public void setPricePerNight(Double pricePerNight) {
            this.pricePerNight = pricePerNight;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    /**
     * Response payload representing accommodation listing details.
     */
    public static class Response {
        private Long id;
        private Long hostId;
        private String hostName;
        private String title;
        private String location;
        private String description;
        private Double pricePerNight;
        private String imageUrl;

        public Response() {}

        public Response(Long id, Long hostId, String hostName, String title, String location, String description, Double pricePerNight, String imageUrl) {
            this.id = id;
            this.hostId = hostId;
            this.hostName = hostName;
            this.title = title;
            this.location = location;
            this.description = description;
            this.pricePerNight = pricePerNight;
            this.imageUrl = imageUrl;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getHostId() {
            return hostId;
        }

        public void setHostId(Long hostId) {
            this.hostId = hostId;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPricePerNight() {
            return pricePerNight;
        }

        public void setPricePerNight(Double pricePerNight) {
            this.pricePerNight = pricePerNight;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}

package com.wanderlink.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Data Transfer Objects for Trip operations.
 */
public class TripDTO {

    /**
     * Request payload for creating a trip.
     */
    public static class CreateRequest {
        @NotNull(message = "User ID is required")
        private Long userId;

        @NotBlank(message = "Destination is required")
        @Size(max = 100, message = "Destination cannot exceed 100 characters")
        private String destination;

        @NotNull(message = "Start date is required")
        private LocalDate startDate;

        @NotNull(message = "End date is required")
        private LocalDate endDate;

        @NotNull(message = "Budget is required")
        @Positive(message = "Budget must be a positive number")
        private Double budget;

        private String interests;
        private String travelStyle;
        private String description;

        public CreateRequest() {}

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Double getBudget() {
            return budget;
        }

        public void setBudget(Double budget) {
            this.budget = budget;
        }

        public String getInterests() {
            return interests;
        }

        public void setInterests(String interests) {
            this.interests = interests;
        }

        public String getTravelStyle() {
            return travelStyle;
        }

        public void setTravelStyle(String travelStyle) {
            this.travelStyle = travelStyle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    /**
     * Response payload for trip details.
     */
    public static class Response {
        private Long id;
        private Long userId;
        private String userName;
        private String userLocation;
        private String destination;
        private LocalDate startDate;
        private LocalDate endDate;
        private Double budget;
        private String interests;
        private String travelStyle;
        private String description;

        public Response() {}

        public Response(Long id, Long userId, String userName, String userLocation, String destination, LocalDate startDate, LocalDate endDate, Double budget, String interests, String travelStyle, String description) {
            this.id = id;
            this.userId = userId;
            this.userName = userName;
            this.userLocation = userLocation;
            this.destination = destination;
            this.startDate = startDate;
            this.endDate = endDate;
            this.budget = budget;
            this.interests = interests;
            this.travelStyle = travelStyle;
            this.description = description;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserLocation() {
            return userLocation;
        }

        public void setUserLocation(String userLocation) {
            this.userLocation = userLocation;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Double getBudget() {
            return budget;
        }

        public void setBudget(Double budget) {
            this.budget = budget;
        }

        public String getInterests() {
            return interests;
        }

        public void setInterests(String interests) {
            this.interests = interests;
        }

        public String getTravelStyle() {
            return travelStyle;
        }

        public void setTravelStyle(String travelStyle) {
            this.travelStyle = travelStyle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

package com.wanderlink.dto;

import com.wanderlink.entity.Trip;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TripDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userAvatar;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;
    private String interests;
    private String travelStyle;
    private String description;
    private LocalDateTime createdAt;

    public TripDTO() {
    }

    public static TripDTO fromEntity(Trip trip) {
        if (trip == null) return null;
        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        if (trip.getUser() != null) {
            dto.setUserId(trip.getUser().getId());
            dto.setUserName(trip.getUser().getFullName());
            dto.setUserEmail(trip.getUser().getEmail());
            dto.setUserAvatar(trip.getUser().getAvatarUrl());
        }
        dto.setDestination(trip.getDestination());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setBudget(trip.getBudget());
        dto.setInterests(trip.getInterests());
        dto.setTravelStyle(trip.getTravelStyle());
        dto.setDescription(trip.getDescription());
        dto.setCreatedAt(trip.getCreatedAt());
        return dto;
    }

    // Getters and Setters

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

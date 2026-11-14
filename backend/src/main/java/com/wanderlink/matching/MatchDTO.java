package com.wanderlink.matching;

import java.time.LocalDate;

/**
 * Data Transfer Object representing traveler compatibility matching result.
 * Note: Compatibility score measures travel compatibility based on destination, dates,
 * interests, budget, and travel style. It is not an identity or safety verification score.
 */
public class MatchDTO {

    private Long userId;
    private String fullName;
    private String location;
    private String bio;
    private String interests;
    private String travelStyle;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;

    // Component scores (0 - 100)
    private Double destinationScore;
    private Double dateScore;
    private Double interestScore;
    private Double budgetScore;
    private Double travelStyleScore;

    // Weighted overall compatibility score (0 - 100)
    private Double matchScore;

    public MatchDTO() {}

    public MatchDTO(Long userId, String fullName, String location, String bio, String interests,
                    String travelStyle, String destination, LocalDate startDate, LocalDate endDate,
                    Double budget, Double destinationScore, Double dateScore, Double interestScore,
                    Double budgetScore, Double travelStyleScore, Double matchScore) {
        this.userId = userId;
        this.fullName = fullName;
        this.location = location;
        this.bio = bio;
        this.interests = interests;
        this.travelStyle = travelStyle;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.destinationScore = destinationScore;
        this.dateScore = dateScore;
        this.interestScore = interestScore;
        this.budgetScore = budgetScore;
        this.travelStyleScore = travelStyleScore;
        this.matchScore = matchScore;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public Double getDestinationScore() {
        return destinationScore;
    }

    public void setDestinationScore(Double destinationScore) {
        this.destinationScore = destinationScore;
    }

    public Double getDateScore() {
        return dateScore;
    }

    public void setDateScore(Double dateScore) {
        this.dateScore = dateScore;
    }

    public Double getInterestScore() {
        return interestScore;
    }

    public void setInterestScore(Double interestScore) {
        this.interestScore = interestScore;
    }

    public Double getBudgetScore() {
        return budgetScore;
    }

    public void setBudgetScore(Double budgetScore) {
        this.budgetScore = budgetScore;
    }

    public Double getTravelStyleScore() {
        return travelStyleScore;
    }

    public void setTravelStyleScore(Double travelStyleScore) {
        this.travelStyleScore = travelStyleScore;
    }

    public Double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Double matchScore) {
        this.matchScore = matchScore;
    }
}

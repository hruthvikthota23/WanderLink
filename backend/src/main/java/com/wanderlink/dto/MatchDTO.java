package com.wanderlink.dto;

import java.util.List;

public class MatchDTO {

    private UserDTO traveler;
    private TripDTO trip;
    private double compatibilityScore;
    private double destinationScore;
    private double dateScore;
    private double interestScore;
    private double budgetScore;
    private double styleScore;
    private List<String> commonInterests;
    private String connectionStatus = "NONE";
    private Long connectionId;

    public MatchDTO() {
    }

    // Getters and Setters

    public UserDTO getTraveler() {
        return traveler;
    }

    public void setTraveler(UserDTO traveler) {
        this.traveler = traveler;
    }

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    public double getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public double getDestinationScore() {
        return destinationScore;
    }

    public void setDestinationScore(double destinationScore) {
        this.destinationScore = destinationScore;
    }

    public double getDateScore() {
        return dateScore;
    }

    public void setDateScore(double dateScore) {
        this.dateScore = dateScore;
    }

    public double getInterestScore() {
        return interestScore;
    }

    public void setInterestScore(double interestScore) {
        this.interestScore = interestScore;
    }

    public double getBudgetScore() {
        return budgetScore;
    }

    public void setBudgetScore(double budgetScore) {
        this.budgetScore = budgetScore;
    }

    public double getStyleScore() {
        return styleScore;
    }

    public void setStyleScore(double styleScore) {
        this.styleScore = styleScore;
    }

    public List<String> getCommonInterests() {
        return commonInterests;
    }

    public void setCommonInterests(List<String> commonInterests) {
        this.commonInterests = commonInterests;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
}

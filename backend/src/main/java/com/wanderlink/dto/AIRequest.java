package com.wanderlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class AIRequest {

    @NotBlank(message = "Message is required")
    @Size(max = 6000, message = "Message must be 6000 characters or less")
    private String message;

    @Valid
    @Size(max = 20, message = "Chat history is limited to the last 20 messages")
    private List<AIMessage> history = new ArrayList<>();

    private String destination;

    private Double budget;

    private Integer days;

    private String travelStyle;

    private String interests;

    public AIRequest() {
    }

    public AIRequest(String message, String destination, Double budget, Integer days) {
        this.message = message;
        this.destination = destination;
        this.budget = budget;
        this.days = days;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AIMessage> getHistory() {
        return history;
    }

    public void setHistory(List<AIMessage> history) {
        this.history = history == null ? new ArrayList<>() : history;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getTravelStyle() {
        return travelStyle;
    }

    public void setTravelStyle(String travelStyle) {
        this.travelStyle = travelStyle;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
}

package com.wanderlink.dto;

import java.util.List;

public class AIResponse {

    private String reply;
    private String destination;
    private List<String> suggestions;
    private List<String> itinerary;
    private Double estimatedBudget;
    private boolean fallbackUsed;

    public AIResponse() {
    }

    public AIResponse(String reply, String destination, List<String> suggestions) {
        this.reply = reply;
        this.destination = destination;
        this.suggestions = suggestions;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<String> itinerary) {
        this.itinerary = itinerary;
    }

    public Double getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(Double estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public boolean isFallbackUsed() {
        return fallbackUsed;
    }

    public void setFallbackUsed(boolean fallbackUsed) {
        this.fallbackUsed = fallbackUsed;
    }
}

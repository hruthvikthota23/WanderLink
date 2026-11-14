package com.wanderlink.dto;

import com.wanderlink.entity.Guide;
import java.time.LocalDateTime;

public class GuideDTO {

    private Long id;
    private String guideName;
    private String destination;
    private String languages;
    private Integer experienceYears;
    private Double pricePerDay;
    private String bio;
    private String contactEmail;
    private String contactPhone;
    private String imageUrl;
    private Double rating;
    private LocalDateTime createdAt;

    public GuideDTO() {
    }

    public static GuideDTO fromEntity(Guide guide) {
        if (guide == null) return null;
        GuideDTO dto = new GuideDTO();
        dto.setId(guide.getId());
        dto.setGuideName(guide.getGuideName());
        dto.setDestination(guide.getDestination());
        dto.setLanguages(guide.getLanguages());
        dto.setExperienceYears(guide.getExperienceYears());
        dto.setPricePerDay(guide.getPricePerDay());
        dto.setBio(guide.getBio());
        dto.setContactEmail(guide.getContactEmail());
        dto.setContactPhone(guide.getContactPhone());
        dto.setImageUrl(guide.getImageUrl());
        dto.setRating(guide.getRating());
        dto.setCreatedAt(guide.getCreatedAt());
        return dto;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

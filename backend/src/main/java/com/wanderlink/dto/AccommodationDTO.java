package com.wanderlink.dto;

import com.wanderlink.entity.Accommodation;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AccommodationDTO {

    private Long id;
    private Long hostId;
    private String hostName;
    private String hostAvatar;
    @NotBlank(message = "Property name is required")
    private String propertyName;
    @NotBlank(message = "Location is required")
    private String location;
    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per night must be greater than 0")
    private Double pricePerNight;
    private String description;
    private String imageUrl;
    private String amenities;
    private String contactInfo;
    private LocalDateTime createdAt;

    public AccommodationDTO() {
    }

    public static AccommodationDTO fromEntity(Accommodation acc) {
        if (acc == null) return null;
        AccommodationDTO dto = new AccommodationDTO();
        dto.setId(acc.getId());
        if (acc.getHost() != null) {
            dto.setHostId(acc.getHost().getId());
            dto.setHostName(acc.getHost().getFullName());
            dto.setHostAvatar(acc.getHost().getAvatarUrl());
        }
        dto.setPropertyName(acc.getPropertyName());
        dto.setLocation(acc.getLocation());
        dto.setPricePerNight(acc.getPricePerNight());
        dto.setDescription(acc.getDescription());
        dto.setImageUrl(acc.getImageUrl());
        dto.setAmenities(acc.getAmenities());
        dto.setContactInfo(acc.getContactInfo());
        dto.setCreatedAt(acc.getCreatedAt());
        return dto;
    }

    // Getters and Setters

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

    public String getHostAvatar() {
        return hostAvatar;
    }

    public void setHostAvatar(String hostAvatar) {
        this.hostAvatar = hostAvatar;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
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

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

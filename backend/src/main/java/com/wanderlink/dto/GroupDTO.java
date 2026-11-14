package com.wanderlink.dto;

import com.wanderlink.entity.TravelGroup;
import java.time.LocalDateTime;

public class GroupDTO {

    private Long id;
    private String name;
    private String destination;
    private String description;
    private String imageUrl;
    private Long creatorId;
    private String creatorName;
    private long memberCount;
    private boolean isMember;
    private LocalDateTime createdAt;

    public GroupDTO() {
    }

    public static GroupDTO fromEntity(TravelGroup group, long memberCount, boolean isMember) {
        if (group == null) return null;
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDestination(group.getDestination());
        dto.setDescription(group.getDescription());
        dto.setImageUrl(group.getImageUrl());
        if (group.getCreator() != null) {
            dto.setCreatorId(group.getCreator().getId());
            dto.setCreatorName(group.getCreator().getFullName());
        }
        dto.setMemberCount(memberCount);
        dto.setMember(isMember);
        dto.setCreatedAt(group.getCreatedAt());
        return dto;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(long memberCount) {
        this.memberCount = memberCount;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

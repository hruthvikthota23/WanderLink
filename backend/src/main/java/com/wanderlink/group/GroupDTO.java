package com.wanderlink.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Objects for Travel Group operations.
 */
public class GroupDTO {

    /**
     * Request payload for creating a new travel group.
     */
    public static class CreateRequest {
        @NotBlank(message = "Group name is required")
        @Size(max = 150, message = "Group name cannot exceed 150 characters")
        private String name;

        @NotBlank(message = "Destination is required")
        @Size(max = 100, message = "Destination cannot exceed 100 characters")
        private String destination;

        private String description;

        @NotNull(message = "Creator user ID is required")
        private Long createdBy;

        public CreateRequest() {}

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

        public Long getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Long createdBy) {
            this.createdBy = createdBy;
        }
    }

    /**
     * Request payload for joining an existing travel group.
     */
    public static class JoinRequest {
        @NotNull(message = "User ID is required")
        private Long userId;

        public JoinRequest() {}

        public JoinRequest(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    /**
     * Response payload representing travel group details.
     */
    public static class Response {
        private Long id;
        private String name;
        private String destination;
        private String description;
        private Long createdById;
        private String createdByName;
        private Integer memberCount;

        public Response() {}

        public Response(Long id, String name, String destination, String description, Long createdById, String createdByName, Integer memberCount) {
            this.id = id;
            this.name = name;
            this.destination = destination;
            this.description = description;
            this.createdById = createdById;
            this.createdByName = createdByName;
            this.memberCount = memberCount;
        }

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

        public Long getCreatedById() {
            return createdById;
        }

        public void setCreatedById(Long createdById) {
            this.createdById = createdById;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

        public Integer getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(Integer memberCount) {
            this.memberCount = memberCount;
        }
    }

    /**
     * Response payload representing group member record.
     */
    public static class MemberResponse {
        private Long id;
        private Long groupId;
        private String groupName;
        private Long userId;
        private String userName;
        private LocalDateTime joinedAt;

        public MemberResponse() {}

        public MemberResponse(Long id, Long groupId, String groupName, Long userId, String userName, LocalDateTime joinedAt) {
            this.id = id;
            this.groupId = groupId;
            this.groupName = groupName;
            this.userId = userId;
            this.userName = userName;
            this.joinedAt = joinedAt;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
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

        public LocalDateTime getJoinedAt() {
            return joinedAt;
        }

        public void setJoinedAt(LocalDateTime joinedAt) {
            this.joinedAt = joinedAt;
        }
    }
}

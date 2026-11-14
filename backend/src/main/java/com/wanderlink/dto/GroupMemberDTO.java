package com.wanderlink.dto;

import com.wanderlink.entity.GroupMember;
import java.time.LocalDateTime;

public class GroupMemberDTO {

    private Long id;
    private Long groupId;
    private UserDTO user;
    private String role;
    private LocalDateTime joinedAt;

    public GroupMemberDTO() {
    }

    public static GroupMemberDTO fromEntity(GroupMember member) {
        if (member == null) return null;
        GroupMemberDTO dto = new GroupMemberDTO();
        dto.setId(member.getId());
        if (member.getGroup() != null) {
            dto.setGroupId(member.getGroup().getId());
        }
        dto.setUser(UserDTO.fromEntity(member.getUser()));
        dto.setRole(member.getRole());
        dto.setJoinedAt(member.getJoinedAt());
        return dto;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}

package com.wanderlink.controller;

import com.wanderlink.dto.ApiResponse;
import com.wanderlink.dto.GroupDTO;
import com.wanderlink.dto.GroupMemberDTO;
import com.wanderlink.security.UserPrincipal;
import com.wanderlink.service.TravelGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final TravelGroupService travelGroupService;

    public GroupController(TravelGroupService travelGroupService) {
        this.travelGroupService = travelGroupService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getAllGroups(
            @RequestParam(required = false) String destination,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        List<GroupDTO> groups = travelGroupService.getAllGroups(destination, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("Groups retrieved successfully", groups));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GroupDTO>> getGroupById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = principal != null ? principal.getId() : null;
        GroupDTO group = travelGroupService.getGroupById(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success("Group retrieved successfully", group));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GroupDTO>> createGroup(
            @Valid @RequestBody GroupDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long creatorId = principal != null ? principal.getId() : 1L;
        GroupDTO created = travelGroupService.createGroup(dto, creatorId);
        return new ResponseEntity<>(ApiResponse.success("Group created successfully", created), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<ApiResponse<Void>> joinGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getId() : 1L;
        travelGroupService.joinGroup(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Joined group successfully", null));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getId() : 1L;
        travelGroupService.leaveGroup(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Left group successfully", null));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<GroupMemberDTO>>> getGroupMembers(@PathVariable Long id) {
        List<GroupMemberDTO> members = travelGroupService.getGroupMembers(id);
        return ResponseEntity.ok(ApiResponse.success("Group members retrieved successfully", members));
    }
}

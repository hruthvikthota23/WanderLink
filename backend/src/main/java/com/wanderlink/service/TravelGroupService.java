package com.wanderlink.service;

import com.wanderlink.dto.GroupDTO;
import com.wanderlink.dto.GroupMemberDTO;
import com.wanderlink.entity.GroupMember;
import com.wanderlink.entity.TravelGroup;
import com.wanderlink.entity.User;
import com.wanderlink.exception.BadRequestException;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.GroupMemberRepository;
import com.wanderlink.repository.TravelGroupRepository;
import com.wanderlink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelGroupService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public TravelGroupService(TravelGroupRepository travelGroupRepository,
                              GroupMemberRepository groupMemberRepository,
                              UserRepository userRepository) {
        this.travelGroupRepository = travelGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    public List<GroupDTO> getAllGroups(String destination, Long currentUserId) {
        List<TravelGroup> groups;
        if (destination != null && !destination.isBlank()) {
            groups = travelGroupRepository.findByDestinationContainingIgnoreCase(destination.trim());
        } else {
            groups = travelGroupRepository.findAll();
        }

        return groups.stream().map(g -> {
            long count = groupMemberRepository.countByGroupId(g.getId());
            boolean isMember = currentUserId != null && groupMemberRepository.existsByGroupIdAndUserId(g.getId(), currentUserId);
            return GroupDTO.fromEntity(g, count, isMember);
        }).collect(Collectors.toList());
    }

    public GroupDTO getGroupById(Long id, Long currentUserId) {
        TravelGroup group = travelGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel group not found with id: " + id));

        long count = groupMemberRepository.countByGroupId(group.getId());
        boolean isMember = currentUserId != null && groupMemberRepository.existsByGroupIdAndUserId(group.getId(), currentUserId);
        return GroupDTO.fromEntity(group, count, isMember);
    }

    @Transactional
    public GroupDTO createGroup(GroupDTO dto, Long creatorId) {
        User creator = null;
        if (creatorId != null) {
            creator = userRepository.findById(creatorId).orElse(null);
        }

        TravelGroup group = new TravelGroup();
        group.setName(dto.getName());
        group.setDestination(dto.getDestination());
        group.setDescription(dto.getDescription());
        group.setImageUrl(dto.getImageUrl() != null && !dto.getImageUrl().isBlank() ? dto.getImageUrl() :
                "https://images.unsplash.com/photo-1526772662000-3f88f10405ff?w=500&auto=format&fit=crop&q=80");
        group.setCreator(creator);

        TravelGroup saved = travelGroupRepository.save(group);

        // Add creator as initial member with role CREATOR
        if (creator != null) {
            GroupMember member = new GroupMember(saved, creator, "CREATOR");
            groupMemberRepository.save(member);
        }

        return GroupDTO.fromEntity(saved, 1, true);
    }

    @Transactional
    public void joinGroup(Long groupId, Long userId) {
        TravelGroup group = travelGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Travel group not found with id: " + groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new BadRequestException("You are already a member of this group");
        }

        GroupMember member = new GroupMember(group, user, "MEMBER");
        groupMemberRepository.save(member);
    }

    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new BadRequestException("You are not a member of this group");
        }

        groupMemberRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    public List<GroupMemberDTO> getGroupMembers(Long groupId) {
        if (!travelGroupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Travel group not found with id: " + groupId);
        }

        return groupMemberRepository.findByGroupId(groupId).stream()
                .map(GroupMemberDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

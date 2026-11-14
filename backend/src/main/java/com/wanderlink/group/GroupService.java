package com.wanderlink.group;

import com.wanderlink.common.exception.DuplicateResourceException;
import com.wanderlink.common.exception.ResourceNotFoundException;
import com.wanderlink.user.User;
import com.wanderlink.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing travel groups and memberships.
 */
@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserService userService;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userService = userService;
    }

    /**
     * Creates a new travel group and automatically enrolls the creator as the founding member.
     */
    public GroupDTO.Response createGroup(GroupDTO.CreateRequest request) {
        User creator = userService.findEntityById(request.getCreatedBy());

        TravelGroup group = new TravelGroup(
                request.getName().trim(),
                request.getDestination().trim(),
                request.getDescription() != null ? request.getDescription().trim() : "",
                creator
        );
        group.setMemberCount(1);
        TravelGroup savedGroup = groupRepository.save(group);

        // Add creator as the initial member
        GroupMember founderMember = new GroupMember(savedGroup, creator);
        groupMemberRepository.save(founderMember);

        return mapToResponse(savedGroup);
    }

    /**
     * Retrieves all travel groups.
     */
    @Transactional(readOnly = true)
    public List<GroupDTO.Response> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Enrolls a user into an existing travel group.
     * Prevents duplicate memberships.
     */
    public GroupDTO.MemberResponse joinGroup(Long groupId, Long userId) {
        TravelGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("TravelGroup", "id", groupId));

        User user = userService.findEntityById(userId);

        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new DuplicateResourceException("User is already a member of this travel group.");
        }

        GroupMember newMember = new GroupMember(group, user);
        GroupMember savedMember = groupMemberRepository.save(newMember);

        // Update member count
        group.setMemberCount(group.getMemberCount() + 1);
        groupRepository.save(group);

        return new GroupDTO.MemberResponse(
                savedMember.getId(),
                group.getId(),
                group.getName(),
                user.getId(),
                user.getFullName(),
                savedMember.getJoinedAt()
        );
    }

    /**
     * Maps TravelGroup entity to GroupDTO.Response.
     */
    public GroupDTO.Response mapToResponse(TravelGroup group) {
        User creator = group.getCreatedBy();
        return new GroupDTO.Response(
                group.getId(),
                group.getName(),
                group.getDestination(),
                group.getDescription(),
                creator.getId(),
                creator.getFullName(),
                group.getMemberCount()
        );
    }
}

package com.wanderlink.controller;

import com.wanderlink.dto.ApiResponse;
import com.wanderlink.dto.ConnectionDTO;
import com.wanderlink.dto.ConnectionRequest;
import com.wanderlink.exception.ForbiddenException;
import com.wanderlink.exception.UnauthorizedException;
import com.wanderlink.security.UserPrincipal;
import com.wanderlink.service.ConnectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConnectionDTO>> sendConnectionRequest(
            @Valid @RequestBody ConnectionRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        ConnectionDTO connection = connectionService.sendConnectionRequest(request, currentUserId(principal));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Connection request sent", connection));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConnectionDTO>>> getCurrentUserConnections(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success("Connections retrieved",
                connectionService.getUserConnections(currentUserId(principal))));
    }

    /** Kept for existing frontend clients; callers may only read their own connections. */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ConnectionDTO>>> getUserConnections(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long currentUserId = currentUserId(principal);
        if (!currentUserId.equals(userId)) {
            throw new ForbiddenException("You can only view your own connection requests");
        }
        return ResponseEntity.ok(ApiResponse.success("Connections retrieved",
                connectionService.getUserConnections(currentUserId)));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<ConnectionDTO>> acceptConnection(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        ConnectionDTO connection = connectionService.acceptConnection(id, currentUserId(principal));
        return ResponseEntity.ok(ApiResponse.success("Connection request accepted", connection));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ConnectionDTO>> rejectConnection(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        ConnectionDTO connection = connectionService.rejectConnection(id, currentUserId(principal));
        return ResponseEntity.ok(ApiResponse.success("Connection request rejected", connection));
    }

    private Long currentUserId(UserPrincipal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Please sign in to manage connections");
        }
        return principal.getId();
    }
}

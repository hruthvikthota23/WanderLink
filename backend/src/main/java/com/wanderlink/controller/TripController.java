package com.wanderlink.controller;

import com.wanderlink.dto.ApiResponse;
import com.wanderlink.dto.TripDTO;
import com.wanderlink.dto.TripRequest;
import com.wanderlink.exception.UnauthorizedException;
import com.wanderlink.security.UserPrincipal;
import com.wanderlink.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TripDTO>> createTrip(
            @Valid @RequestBody TripRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = currentUserId(principal);
        TripDTO created = tripService.createTrip(request, userId);
        return new ResponseEntity<>(ApiResponse.success("Trip created successfully", created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TripDTO>>> getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips();
        return ResponseEntity.ok(ApiResponse.success("Trips retrieved successfully", trips));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripDTO>> getTripById(@PathVariable Long id) {
        TripDTO trip = tripService.getTripById(id);
        return ResponseEntity.ok(ApiResponse.success("Trip retrieved successfully", trip));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TripDTO>>> getTripsByUser(@PathVariable Long userId) {
        List<TripDTO> trips = tripService.getTripsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("User trips retrieved successfully", trips));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripDTO>> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = currentUserId(principal);
        TripDTO updated = tripService.updateTrip(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Trip updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = currentUserId(principal);
        tripService.deleteTrip(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Trip deleted successfully", null));
    }

    private Long currentUserId(UserPrincipal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Please sign in to manage trips");
        }
        return principal.getId();
    }
}

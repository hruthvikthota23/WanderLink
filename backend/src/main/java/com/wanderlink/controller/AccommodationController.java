package com.wanderlink.controller;

import com.wanderlink.dto.AccommodationDTO;
import com.wanderlink.dto.ApiResponse;
import com.wanderlink.security.UserPrincipal;
import com.wanderlink.service.AccommodationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccommodationDTO>>> getAccommodations(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(ApiResponse.success("Accommodations retrieved",
                accommodationService.getAllAccommodations(location, maxPrice)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccommodationDTO>> getAccommodation(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Accommodation retrieved",
                accommodationService.getAccommodationById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccommodationDTO>> createAccommodation(
            @Valid @RequestBody AccommodationDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        AccommodationDTO created = accommodationService.createAccommodation(dto,
                principal == null ? null : principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Accommodation created", created));
    }
}

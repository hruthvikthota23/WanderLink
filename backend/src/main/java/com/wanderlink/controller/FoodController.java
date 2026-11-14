package com.wanderlink.controller;

import com.wanderlink.dto.ApiResponse;
import com.wanderlink.dto.FoodListingDTO;
import com.wanderlink.security.UserPrincipal;
import com.wanderlink.service.FoodListingService;
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
@RequestMapping("/api/food")
public class FoodController {

    private final FoodListingService foodListingService;

    public FoodController(FoodListingService foodListingService) {
        this.foodListingService = foodListingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodListingDTO>>> getFoodListings(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String cuisine) {
        return ResponseEntity.ok(ApiResponse.success("Food listings retrieved",
                foodListingService.getAllFoodListings(location, cuisine)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodListingDTO>> getFoodListing(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Food listing retrieved",
                foodListingService.getFoodListingById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FoodListingDTO>> createFoodListing(
            @Valid @RequestBody FoodListingDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        FoodListingDTO created = foodListingService.createFoodListing(dto,
                principal == null ? null : principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Food listing created", created));
    }
}

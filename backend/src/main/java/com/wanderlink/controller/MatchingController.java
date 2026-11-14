package com.wanderlink.controller;

import com.wanderlink.dto.ApiResponse;
import com.wanderlink.dto.MatchDTO;
import com.wanderlink.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MatchDTO>>> getMatchesForTrip(@RequestParam Long tripId) {
        List<MatchDTO> matches = matchingService.findMatchesForTrip(tripId);
        return ResponseEntity.ok(ApiResponse.success("Travel buddy matches calculated successfully", matches));
    }
}

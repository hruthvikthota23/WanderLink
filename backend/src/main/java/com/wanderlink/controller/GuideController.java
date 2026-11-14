package com.wanderlink.controller;

import com.wanderlink.dto.ApiResponse;
import com.wanderlink.dto.GuideDTO;
import com.wanderlink.security.UserPrincipal;
import com.wanderlink.service.GuideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
public class GuideController {

    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GuideDTO>>> getAllGuides(
            @RequestParam(required = false) String destination) {
        List<GuideDTO> list = guideService.getAllGuides(destination);
        return ResponseEntity.ok(ApiResponse.success("Guides retrieved successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GuideDTO>> getGuideById(@PathVariable Long id) {
        GuideDTO guide = guideService.getGuideById(id);
        return ResponseEntity.ok(ApiResponse.success("Guide retrieved successfully", guide));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GuideDTO>> createGuide(
            @Valid @RequestBody GuideDTO dto,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getId() : null;
        GuideDTO created = guideService.createGuide(dto, userId);
        return new ResponseEntity<>(ApiResponse.success("Guide created successfully", created), HttpStatus.CREATED);
    }
}

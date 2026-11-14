package com.wanderlink.controller;

import com.wanderlink.dto.AIRequest;
import com.wanderlink.dto.AIResponse;
import com.wanderlink.service.AIService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/assistant")
    public ResponseEntity<AIResponse> getAssistantResponse(@Valid @RequestBody AIRequest request) {
        return ResponseEntity.ok(aiService.generateAssistantReply(request));
    }
}

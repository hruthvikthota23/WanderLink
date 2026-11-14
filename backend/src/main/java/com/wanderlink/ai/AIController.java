package com.wanderlink.ai;

import com.wanderlink.ai.dto.AIRequest;
import com.wanderlink.ai.dto.AIResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * REST Controller providing the agreed AI Assistant integration contract.
 * Developed in coordination with Person 4 (AI Developer).
 */
@RestController
@RequestMapping("/api/ai")
public class AIController {

    /**
     * AI travel assistant endpoint contract.
     * Serves as the integration point between frontend and Person 4's AI engine.
     */
    @PostMapping("/assistant")
    public ResponseEntity<AIResponse> getAssistantResponse(@Valid @RequestBody AIRequest request) {
        String prompt = request.getMessage().toLowerCase();
        String destination = "Goa";
        List<String> suggestions;

        if (prompt.contains("manali")) {
            destination = "Manali";
            suggestions = Arrays.asList(
                    "Trek to Jogini Waterfall",
                    "Try authentic Himachali Siddu at local cafes",
                    "Join a Solang Valley adventure travel group"
            );
        } else if (prompt.contains("kerala") || prompt.contains("kochi")) {
            destination = "Kerala";
            suggestions = Arrays.asList(
                    "Book a traditional backwaters stay in Alleppey",
                    "Savor local Malabar fish curry and appam",
                    "Connect with solo travelers heading to Munnar tea trails"
            );
        } else {
            // Default to Goa for hackathon showcase
            destination = "Goa";
            suggestions = Arrays.asList(
                    "Visit local beaches (Palolem and Morjim)",
                    "Explore local Goan seafood and homemade vindaloo",
                    "Join a travel group for North Goa sunset exploration"
            );
        }

        AIResponse response = new AIResponse(
                "Here is your suggested " + destination + " itinerary based on your preferences.",
                destination,
                suggestions
        );

        return ResponseEntity.ok(response);
    }
}

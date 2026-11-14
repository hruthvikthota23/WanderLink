package com.wanderlink;

import com.wanderlink.dto.AIRequest;
import com.wanderlink.dto.AIResponse;
import com.wanderlink.service.AIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AIServiceTest {

    private AIService aiService;

    @BeforeEach
    void setUp() {
        aiService = new AIService();
    }

    @Test
    @DisplayName("AI assistant returns coherent Goa suggestions and itinerary with fallback")
    void testGoaAssistantReply() {
        AIRequest request = new AIRequest("Plan a 5-day budget trip to Goa", "Goa", 12000.0, 5);

        AIResponse response = aiService.generateAssistantReply(request);

        assertNotNull(response);
        assertEquals("Goa", response.getDestination());
        assertTrue(response.isFallbackUsed());
        assertFalse(response.getSuggestions().isEmpty());
        assertTrue(response.getItinerary().size() >= 5);
        assertTrue(response.getReply().contains("Goa"));
        assertTrue(response.getReply().toLowerCase().contains("itinerary"));
    }
}

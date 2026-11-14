package com.wanderlink.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wanderlink.dto.AIMessage;
import com.wanderlink.dto.AIRequest;
import com.wanderlink.dto.AIResponse;
import com.wanderlink.exception.AIProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {

    private static final String DEFAULT_INSTRUCTIONS = """
            You are WanderLink's travel assistant. Answer the user's actual question directly and use the
            conversation history for follow-up questions. Do not force every response into an itinerary.
            When the user asks for an itinerary, make the number of days, destination, budget, travel style,
            and interests they mention drive the plan. Return a concise helpful reply, useful community
            recommendations, and day-by-day steps only when relevant. Never claim a booking was made or
            invent live availability, exact prices, or current opening hours. If information is missing,
            state reasonable assumptions or ask a short follow-up question. Return the requested JSON object.
            """;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final String apiUrl;
    private final boolean localTestMode;

    @Autowired
    public AIService(RestClient.Builder restClientBuilder,
                     ObjectMapper objectMapper,
                     @Value("${wanderlink.ai.api-key:}") String apiKey,
                     @Value("${wanderlink.ai.model:gpt-6-luna}") String model,
                     @Value("${wanderlink.ai.api-url:https://api.openai.com/v1/responses}") String apiUrl) {
        this(restClientBuilder.build(), objectMapper, apiKey, model, apiUrl, false);
    }

    /** Keeps the existing direct unit-test construction independent from provider credentials. */
    public AIService() {
        this(RestClient.create(), new ObjectMapper(), "", "gpt-6-luna",
                "https://api.openai.com/v1/responses", true);
    }

    private AIService(RestClient restClient, ObjectMapper objectMapper, String apiKey,
                      String model, String apiUrl, boolean localTestMode) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.model = model;
        this.apiUrl = apiUrl;
        this.localTestMode = localTestMode;
    }

    public AIResponse generateAssistantReply(AIRequest request) {
        if (localTestMode) {
            return buildTestFallback(request);
        }
        if (apiKey.isBlank()) {
            throw new AIProviderException(
                    "AI is not configured. Set WANDERLINK_AI_API_KEY in the backend environment and restart it.",
                    null);
        }

        try {
            JsonNode providerResponse = restClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .body(buildPayload(request))
                    .retrieve()
                    .body(JsonNode.class);

            String outputText = extractOutputText(providerResponse);
            JsonNode answer = objectMapper.readTree(outputText);
            return toAIResponse(answer);
        } catch (RestClientResponseException ex) {
            throw new AIProviderException(
                    "The AI provider returned HTTP " + ex.getStatusCode().value()
                            + ". Check the API key, model setting, and account quota.", ex);
        } catch (AIProviderException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AIProviderException(
                    "Could not get a response from the AI provider. Check the backend network connection and settings.", ex);
        }
    }

    private ObjectNode buildPayload(AIRequest request) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("model", model);
        payload.put("instructions", buildInstructions(request));

        ArrayNode input = payload.putArray("input");
        List<AIMessage> history = request.getHistory();
        if (history != null) {
            int first = Math.max(0, history.size() - 12);
            for (int i = first; i < history.size(); i++) {
                AIMessage message = history.get(i);
                if (message != null && message.getRole() != null && message.getContent() != null
                        && !message.getContent().isBlank()) {
                    addInputMessage(input, message.getRole(), message.getContent());
                }
            }
        }
        addInputMessage(input, "user", request.getMessage());

        ObjectNode format = payload.putObject("text").putObject("format");
        format.put("type", "json_schema");
        format.put("name", "wanderlink_travel_answer");
        format.put("strict", true);
        format.set("schema", createResponseSchema());
        payload.put("max_output_tokens", 1200);
        return payload;
    }

    private String buildInstructions(AIRequest request) {
        StringBuilder instructions = new StringBuilder(DEFAULT_INSTRUCTIONS);
        List<String> context = new ArrayList<>();
        if (request.getDestination() != null && !request.getDestination().isBlank()) {
            context.add("Destination preference: " + request.getDestination().trim());
        }
        if (request.getBudget() != null) {
            context.add("Budget: " + request.getBudget());
        }
        if (request.getDays() != null) {
            context.add("Trip length: " + request.getDays() + " days");
        }
        if (request.getTravelStyle() != null && !request.getTravelStyle().isBlank()) {
            context.add("Travel style: " + request.getTravelStyle().trim());
        }
        if (request.getInterests() != null && !request.getInterests().isBlank()) {
            context.add("Interests: " + request.getInterests().trim());
        }
        if (!context.isEmpty()) {
            instructions.append("\n\nUser-provided trip context:\n- ")
                    .append(String.join("\n- ", context));
        }
        return instructions.toString();
    }

    private void addInputMessage(ArrayNode input, String role, String content) {
        ObjectNode item = input.addObject();
        item.put("role", role);
        item.put("content", content);
    }

    private ObjectNode createResponseSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        schema.put("additionalProperties", false);
        ObjectNode properties = schema.putObject("properties");
        properties.putObject("reply").put("type", "string");
        properties.putObject("destination").put("type", "string");
        ObjectNode suggestions = properties.putObject("suggestions");
        suggestions.put("type", "array");
        suggestions.putObject("items").put("type", "string");
        ObjectNode itinerary = properties.putObject("itinerary");
        itinerary.put("type", "array");
        itinerary.putObject("items").put("type", "string");
        ObjectNode budget = properties.putObject("estimatedBudget");
        ArrayNode budgetTypes = budget.putArray("type");
        budgetTypes.add("number");
        budgetTypes.add("null");
        properties.putObject("fallbackUsed").put("type", "boolean");
        ArrayNode required = schema.putArray("required");
        required.add("reply");
        required.add("destination");
        required.add("suggestions");
        required.add("itinerary");
        required.add("estimatedBudget");
        required.add("fallbackUsed");
        return schema;
    }

    private String extractOutputText(JsonNode response) {
        if (response == null) {
            throw new AIProviderException("The AI provider returned an empty response.", null);
        }
        JsonNode outputText = response.get("output_text");
        if (outputText != null && outputText.isTextual() && !outputText.asText().isBlank()) {
            return outputText.asText();
        }
        for (JsonNode outputItem : response.path("output")) {
            for (JsonNode content : outputItem.path("content")) {
                if ("output_text".equals(content.path("type").asText())
                        && content.path("text").isTextual()) {
                    return content.path("text").asText();
                }
            }
        }
        throw new AIProviderException("The AI provider response did not contain a text answer.", null);
    }

    private AIResponse toAIResponse(JsonNode answer) {
        AIResponse response = new AIResponse();
        response.setReply(answer.path("reply").asText("I couldn't prepare a response just now."));
        response.setDestination(answer.path("destination").asText(null));
        response.setSuggestions(readStringList(answer.path("suggestions")));
        response.setItinerary(readStringList(answer.path("itinerary")));
        JsonNode estimatedBudget = answer.get("estimatedBudget");
        response.setEstimatedBudget(estimatedBudget == null || estimatedBudget.isNull()
                ? null : estimatedBudget.asDouble());
        response.setFallbackUsed(false);
        return response;
    }

    private List<String> readStringList(JsonNode values) {
        List<String> result = new ArrayList<>();
        if (values != null && values.isArray()) {
            values.forEach(value -> {
                if (value.isTextual() && !value.asText().isBlank()) {
                    result.add(value.asText());
                }
            });
        }
        return result;
    }

    /** Deterministic response only for the existing unit tests that construct this service directly. */
    private AIResponse buildTestFallback(AIRequest request) {
        String destination = request.getDestination() == null || request.getDestination().isBlank()
                ? "Goa" : request.getDestination().trim();
        int days = request.getDays() == null ? 3 : Math.max(1, Math.min(request.getDays(), 14));
        List<String> suggestions = List.of(
                "Visit a popular local attraction in " + destination,
                "Try a well-reviewed local meal in " + destination,
                "Explore a neighborhood with a local guide");
        List<String> itinerary = new ArrayList<>();
        for (int day = 0; day < days; day++) {
            itinerary.add("Day " + (day + 1) + ": " + suggestions.get(day % suggestions.size()));
        }
        AIResponse response = new AIResponse();
        response.setReply("Here is a " + days + "-day itinerary for " + destination + " based on your preferences.");
        response.setDestination(destination);
        response.setSuggestions(suggestions);
        response.setItinerary(itinerary);
        response.setEstimatedBudget(request.getBudget());
        response.setFallbackUsed(true);
        return response;
    }
}

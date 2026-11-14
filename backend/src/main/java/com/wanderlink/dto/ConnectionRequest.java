package com.wanderlink.dto;

import jakarta.validation.constraints.NotNull;

public class ConnectionRequest {

    @NotNull(message = "Receiver ID is required")
    private Long receiverId;

    private String message;

    public ConnectionRequest() {
    }

    public ConnectionRequest(Long receiverId, String message) {
        this.receiverId = receiverId;
        this.message = message;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

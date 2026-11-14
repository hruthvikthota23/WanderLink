package com.wanderlink.dto;

import com.wanderlink.entity.Connection;
import java.time.LocalDateTime;

public class ConnectionDTO {

    private Long id;
    private UserDTO sender;
    private UserDTO receiver;
    private Connection.Status status;
    private String message;
    private LocalDateTime createdAt;

    public ConnectionDTO() {
    }

    public static ConnectionDTO fromEntity(Connection connection) {
        if (connection == null) return null;
        ConnectionDTO dto = new ConnectionDTO();
        dto.setId(connection.getId());
        dto.setSender(UserDTO.fromEntity(connection.getSender()));
        dto.setReceiver(UserDTO.fromEntity(connection.getReceiver()));
        dto.setStatus(connection.getStatus());
        dto.setMessage(connection.getMessage());
        dto.setCreatedAt(connection.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

    public UserDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(UserDTO receiver) {
        this.receiver = receiver;
    }

    public Connection.Status getStatus() {
        return status;
    }

    public void setStatus(Connection.Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

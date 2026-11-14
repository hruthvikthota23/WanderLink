package com.wanderlink.service;

import com.wanderlink.dto.ConnectionDTO;
import com.wanderlink.dto.ConnectionRequest;
import com.wanderlink.entity.Connection;
import com.wanderlink.entity.User;
import com.wanderlink.exception.BadRequestException;
import com.wanderlink.exception.ForbiddenException;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.ConnectionRepository;
import com.wanderlink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionService(ConnectionRepository connectionRepository, UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ConnectionDTO sendConnectionRequest(ConnectionRequest request, Long senderId) {
        if (senderId.equals(request.getReceiverId())) {
            throw new BadRequestException("You cannot send a connection request to yourself");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender user not found with id: " + senderId));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver user not found with id: " + request.getReceiverId()));

        // Check if an existing connection already exists in either direction
        Optional<Connection> existing = connectionRepository.findBySenderIdAndReceiverId(senderId, request.getReceiverId());
        if (existing.isEmpty()) {
            existing = connectionRepository.findBySenderIdAndReceiverId(request.getReceiverId(), senderId);
        }

        if (existing.isPresent()) {
            Connection c = existing.get();
            if (c.getStatus() == Connection.Status.PENDING) {
                throw new BadRequestException("A pending connection request already exists between you and this traveler");
            } else if (c.getStatus() == Connection.Status.ACCEPTED) {
                throw new BadRequestException("You are already connected with this traveler");
            } else {
                // If previously rejected, allow re-requesting by updating status back to PENDING
                c.setSender(sender);
                c.setReceiver(receiver);
                c.setStatus(Connection.Status.PENDING);
                c.setMessage(request.getMessage() != null ? request.getMessage() : "Hi! I'd like to connect on WanderLink.");
                return ConnectionDTO.fromEntity(connectionRepository.save(c));
            }
        }

        Connection connection = new Connection();
        connection.setSender(sender);
        connection.setReceiver(receiver);
        connection.setStatus(Connection.Status.PENDING);
        connection.setMessage(request.getMessage() != null ? request.getMessage() : "Hi! I'd like to connect on WanderLink.");

        Connection saved = connectionRepository.save(connection);
        return ConnectionDTO.fromEntity(saved);
    }

    public List<ConnectionDTO> getUserConnections(Long userId) {
        return connectionRepository.findBySenderIdOrReceiverId(userId, userId).stream()
                .map(ConnectionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ConnectionDTO> getSentRequests(Long userId) {
        return connectionRepository.findBySenderId(userId).stream()
                .map(ConnectionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ConnectionDTO> getReceivedRequests(Long userId) {
        return connectionRepository.findByReceiverId(userId).stream()
                .map(ConnectionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConnectionDTO acceptConnection(Long connectionId, Long currentUserId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Connection request not found with id: " + connectionId));

        if (!connection.getReceiver().getId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to accept this connection request");
        }

        connection.setStatus(Connection.Status.ACCEPTED);
        Connection updated = connectionRepository.save(connection);
        return ConnectionDTO.fromEntity(updated);
    }

    @Transactional
    public ConnectionDTO rejectConnection(Long connectionId, Long currentUserId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Connection request not found with id: " + connectionId));

        if (!connection.getReceiver().getId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to reject this connection request");
        }

        connection.setStatus(Connection.Status.REJECTED);
        Connection updated = connectionRepository.save(connection);
        return ConnectionDTO.fromEntity(updated);
    }
}

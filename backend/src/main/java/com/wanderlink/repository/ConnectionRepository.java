package com.wanderlink.repository;

import com.wanderlink.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findBySenderId(Long senderId);
    List<Connection> findByReceiverId(Long receiverId);
    List<Connection> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
    Optional<Connection> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    boolean existsBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, Connection.Status status);
}

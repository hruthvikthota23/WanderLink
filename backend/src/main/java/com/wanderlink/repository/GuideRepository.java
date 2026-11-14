package com.wanderlink.repository;

import com.wanderlink.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByDestinationContainingIgnoreCase(String destination);
}

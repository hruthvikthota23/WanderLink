package com.wanderlink.repository;

import com.wanderlink.entity.TravelGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelGroupRepository extends JpaRepository<TravelGroup, Long> {
    List<TravelGroup> findByDestinationContainingIgnoreCase(String destination);
}

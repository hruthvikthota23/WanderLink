package com.wanderlink.repository;

import com.wanderlink.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByLocationContainingIgnoreCase(String location);
    List<Accommodation> findByPricePerNightLessThanEqual(Double maxPrice);
    List<Accommodation> findByHostId(Long hostId);
}

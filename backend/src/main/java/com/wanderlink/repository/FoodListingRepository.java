package com.wanderlink.repository;

import com.wanderlink.entity.FoodListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodListingRepository extends JpaRepository<FoodListing, Long> {
    List<FoodListing> findByLocationContainingIgnoreCase(String location);
    List<FoodListing> findByCuisineContainingIgnoreCase(String cuisine);
    List<FoodListing> findByProviderId(Long providerId);
}

package com.wanderlink.service;

import com.wanderlink.dto.FoodListingDTO;
import com.wanderlink.entity.FoodListing;
import com.wanderlink.entity.User;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.FoodListingRepository;
import com.wanderlink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class FoodListingService {

    private final FoodListingRepository foodListingRepository;
    private final UserRepository userRepository;

    public FoodListingService(FoodListingRepository foodListingRepository, UserRepository userRepository) {
        this.foodListingRepository = foodListingRepository;
        this.userRepository = userRepository;
    }

    public List<FoodListingDTO> getAllFoodListings(String location, String cuisine) {
        List<FoodListing> list;
        if (location != null && !location.isBlank()) {
            list = foodListingRepository.findByLocationContainingIgnoreCase(location.trim());
            if (cuisine != null && !cuisine.isBlank()) {
                String cuisineFilter = cuisine.trim().toLowerCase(Locale.ROOT);
                list = list.stream()
                        .filter(food -> food.getCuisine() != null
                                && food.getCuisine().toLowerCase(Locale.ROOT).contains(cuisineFilter))
                        .toList();
            }
        } else if (cuisine != null && !cuisine.isBlank()) {
            list = foodListingRepository.findByCuisineContainingIgnoreCase(cuisine.trim());
        } else {
            list = foodListingRepository.findAll();
        }

        return list.stream()
                .map(FoodListingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public FoodListingDTO getFoodListingById(Long id) {
        FoodListing food = foodListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food listing not found with id: " + id));
        return FoodListingDTO.fromEntity(food);
    }

    @Transactional
    public FoodListingDTO createFoodListing(FoodListingDTO dto, Long providerId) {
        User provider = null;
        if (providerId != null) {
            provider = userRepository.findById(providerId).orElse(null);
        }

        FoodListing food = new FoodListing();
        food.setProvider(provider);
        food.setFoodName(dto.getFoodName());
        food.setCuisine(dto.getCuisine());
        food.setLocation(dto.getLocation());
        food.setPrice(dto.getPrice());
        food.setDescription(dto.getDescription());
        food.setImageUrl(dto.getImageUrl() != null && !dto.getImageUrl().isBlank() ? dto.getImageUrl() :
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=80");
        food.setAvailableDays(dto.getAvailableDays());

        FoodListing saved = foodListingRepository.save(food);
        return FoodListingDTO.fromEntity(saved);
    }
}

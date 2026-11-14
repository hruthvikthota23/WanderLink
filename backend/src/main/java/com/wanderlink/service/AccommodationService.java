package com.wanderlink.service;

import com.wanderlink.dto.AccommodationDTO;
import com.wanderlink.entity.Accommodation;
import com.wanderlink.entity.User;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.AccommodationRepository;
import com.wanderlink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

    public AccommodationService(AccommodationRepository accommodationRepository, UserRepository userRepository) {
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
    }

    public List<AccommodationDTO> getAllAccommodations(String location, Double maxPrice) {
        List<Accommodation> results;
        if (location != null && !location.isBlank()) {
            results = accommodationRepository.findByLocationContainingIgnoreCase(location.trim());
        } else if (maxPrice != null) {
            results = accommodationRepository.findByPricePerNightLessThanEqual(maxPrice);
        } else {
            results = accommodationRepository.findAll();
        }

        if (maxPrice != null && location != null && !location.isBlank()) {
            results = results.stream()
                    .filter(stay -> stay.getPricePerNight() != null && stay.getPricePerNight() <= maxPrice)
                    .toList();
        }
        return results.stream().map(AccommodationDTO::fromEntity).toList();
    }

    public AccommodationDTO getAccommodationById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found with id: " + id));
        return AccommodationDTO.fromEntity(accommodation);
    }

    @Transactional
    public AccommodationDTO createAccommodation(AccommodationDTO dto, Long hostId) {
        User host = hostId == null ? null : userRepository.findById(hostId)
                .orElseThrow(() -> new ResourceNotFoundException("Host user not found with id: " + hostId));
        Accommodation accommodation = new Accommodation();
        accommodation.setHost(host);
        accommodation.setPropertyName(dto.getPropertyName().trim());
        accommodation.setLocation(dto.getLocation().trim());
        accommodation.setPricePerNight(dto.getPricePerNight());
        accommodation.setDescription(dto.getDescription());
        accommodation.setImageUrl(dto.getImageUrl());
        accommodation.setAmenities(dto.getAmenities());
        accommodation.setContactInfo(dto.getContactInfo());
        return AccommodationDTO.fromEntity(accommodationRepository.save(accommodation));
    }
}

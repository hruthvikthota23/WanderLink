package com.wanderlink.service;

import com.wanderlink.dto.GuideDTO;
import com.wanderlink.entity.Guide;
import com.wanderlink.entity.User;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.GuideRepository;
import com.wanderlink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuideService {

    private final GuideRepository guideRepository;
    private final UserRepository userRepository;

    public GuideService(GuideRepository guideRepository, UserRepository userRepository) {
        this.guideRepository = guideRepository;
        this.userRepository = userRepository;
    }

    public List<GuideDTO> getAllGuides(String destination) {
        List<Guide> list;
        if (destination != null && !destination.isBlank()) {
            list = guideRepository.findByDestinationContainingIgnoreCase(destination.trim());
        } else {
            list = guideRepository.findAll();
        }

        return list.stream()
                .map(GuideDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public GuideDTO getGuideById(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guide not found with id: " + id));
        return GuideDTO.fromEntity(guide);
    }

    @Transactional
    public GuideDTO createGuide(GuideDTO dto, Long userId) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        Guide guide = new Guide();
        guide.setUser(user);
        guide.setGuideName(dto.getGuideName());
        guide.setDestination(dto.getDestination());
        guide.setLanguages(dto.getLanguages());
        guide.setExperienceYears(dto.getExperienceYears() != null ? dto.getExperienceYears() : 1);
        guide.setPricePerDay(dto.getPricePerDay());
        guide.setBio(dto.getBio());
        guide.setContactEmail(dto.getContactEmail());
        guide.setContactPhone(dto.getContactPhone());
        guide.setImageUrl(dto.getImageUrl() != null && !dto.getImageUrl().isBlank() ? dto.getImageUrl() :
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop&q=80");
        guide.setRating(dto.getRating() != null ? dto.getRating() : 5.0);

        Guide saved = guideRepository.save(guide);
        return GuideDTO.fromEntity(saved);
    }
}

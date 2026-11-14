package com.wanderlink.service;

import com.wanderlink.dto.TripDTO;
import com.wanderlink.dto.TripRequest;
import com.wanderlink.entity.Trip;
import com.wanderlink.entity.User;
import com.wanderlink.exception.BadRequestException;
import com.wanderlink.exception.ForbiddenException;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.TripRepository;
import com.wanderlink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TripDTO createTrip(TripRequest request, Long userId) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setDestination(request.getDestination().trim());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setBudget(request.getBudget());
        trip.setInterests(request.getInterests());
        trip.setTravelStyle(request.getTravelStyle());
        trip.setDescription(request.getDescription());

        Trip saved = tripRepository.save(trip);
        return TripDTO.fromEntity(saved);
    }

    public List<TripDTO> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(TripDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public TripDTO getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));
        return TripDTO.fromEntity(trip);
    }

    public List<TripDTO> getTripsByUserId(Long userId) {
        return tripRepository.findByUserId(userId).stream()
                .map(TripDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripDTO updateTrip(Long id, TripRequest request, Long currentUserId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

        if (!trip.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to update this trip");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        trip.setDestination(request.getDestination().trim());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setBudget(request.getBudget());
        trip.setInterests(request.getInterests());
        trip.setTravelStyle(request.getTravelStyle());
        trip.setDescription(request.getDescription());

        Trip updated = tripRepository.save(trip);
        return TripDTO.fromEntity(updated);
    }

    @Transactional
    public void deleteTrip(Long id, Long currentUserId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

        if (!trip.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to delete this trip");
        }

        tripRepository.delete(trip);
    }
}

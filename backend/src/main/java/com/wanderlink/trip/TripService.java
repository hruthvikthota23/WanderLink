package com.wanderlink.trip;

import com.wanderlink.common.exception.BadRequestException;
import com.wanderlink.common.exception.ResourceNotFoundException;
import com.wanderlink.user.User;
import com.wanderlink.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing trip creation and retrieval.
 */
@Service
@Transactional
public class TripService {

    private final TripRepository tripRepository;
    private final UserService userService;

    public TripService(TripRepository tripRepository, UserService userService) {
        this.tripRepository = tripRepository;
        this.userService = userService;
    }

    /**
     * Creates a new trip for a user.
     */
    public TripDTO.Response createTrip(TripDTO.CreateRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date.");
        }

        User user = userService.findEntityById(request.getUserId());

        Trip trip = new Trip(
                user,
                request.getDestination().trim(),
                request.getStartDate(),
                request.getEndDate(),
                request.getBudget(),
                request.getInterests() != null ? request.getInterests().trim() : "",
                request.getTravelStyle() != null ? request.getTravelStyle().trim() : "",
                request.getDescription() != null ? request.getDescription().trim() : ""
        );

        Trip saved = tripRepository.save(trip);
        return mapToResponse(saved);
    }

    /**
     * Retrieves all trips.
     */
    @Transactional(readOnly = true)
    public List<TripDTO.Response> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a trip by its ID.
     */
    @Transactional(readOnly = true)
    public TripDTO.Response getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
        return mapToResponse(trip);
    }

    /**
     * Retrieves all trips belonging to a user.
     */
    @Transactional(readOnly = true)
    public List<TripDTO.Response> getTripsByUserId(Long userId) {
        // Ensure user exists
        userService.findEntityById(userId);

        return tripRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Internal helper to find Trip entity.
     */
    @Transactional(readOnly = true)
    public Trip findEntityById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", "id", id));
    }

    /**
     * Maps Trip entity to TripDTO.Response.
     */
    public TripDTO.Response mapToResponse(Trip trip) {
        User user = trip.getUser();
        return new TripDTO.Response(
                trip.getId(),
                user.getId(),
                user.getFullName(),
                user.getLocation(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getBudget(),
                trip.getInterests(),
                trip.getTravelStyle(),
                trip.getDescription()
        );
    }
}

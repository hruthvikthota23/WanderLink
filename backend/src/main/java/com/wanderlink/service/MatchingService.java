package com.wanderlink.service;

import com.wanderlink.dto.MatchDTO;
import com.wanderlink.dto.TripDTO;
import com.wanderlink.dto.UserDTO;
import com.wanderlink.entity.Connection;
import com.wanderlink.entity.Trip;
import com.wanderlink.entity.User;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.ConnectionRepository;
import com.wanderlink.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final TripRepository tripRepository;
    private final ConnectionRepository connectionRepository;

    public MatchingService(TripRepository tripRepository, ConnectionRepository connectionRepository) {
        this.tripRepository = tripRepository;
        this.connectionRepository = connectionRepository;
    }

    public List<MatchDTO> findMatchesForTrip(Long tripId) {
        Trip baseTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        Long currentUserId = baseTrip.getUser().getId();

        // 1. Retrieve all trips excluding the current user's trips
        List<Trip> candidateTrips = tripRepository.findByUserIdNot(currentUserId);

        Map<Long, MatchDTO> bestMatchesByUser = new HashMap<>();

        for (Trip candidate : candidateTrips) {
            User candidateUser = candidate.getUser();
            if (candidateUser == null || candidateUser.getId().equals(currentUserId)) {
                continue;
            }

            // Calculate component scores (each 0 - 100)
            double destinationScore = calculateDestinationScore(baseTrip.getDestination(), candidate.getDestination());
            // Trips to unrelated destinations are not meaningful buddy matches.
            if (destinationScore == 0.0) {
                continue;
            }
            double dateScore = calculateDateScore(baseTrip.getStartDate(), baseTrip.getEndDate(),
                    candidate.getStartDate(), candidate.getEndDate());

            List<String> commonInterests = findCommonInterests(baseTrip, candidate);
            double interestScore = calculateInterestScore(baseTrip, candidate, commonInterests);

            double budgetScore = calculateBudgetScore(baseTrip.getBudget(), candidate.getBudget());
            double styleScore = calculateStyleScore(baseTrip.getTravelStyle(), candidate.getTravelStyle());

            // Weighted formula: Score = 0.30D + 0.25T + 0.20I + 0.15B + 0.10S
            double totalScore = (0.30 * destinationScore)
                    + (0.25 * dateScore)
                    + (0.20 * interestScore)
                    + (0.15 * budgetScore)
                    + (0.10 * styleScore);

            totalScore = Math.max(0.0, Math.min(100.0, Math.round(totalScore * 10.0) / 10.0));

            // Check existing connection status
            Optional<Connection> connOpt = connectionRepository.findBySenderIdAndReceiverId(currentUserId, candidateUser.getId());
            if (connOpt.isEmpty()) {
                connOpt = connectionRepository.findBySenderIdAndReceiverId(candidateUser.getId(), currentUserId);
            }

            MatchDTO matchDTO = new MatchDTO();
            matchDTO.setTraveler(UserDTO.fromEntity(candidateUser));
            matchDTO.setTrip(TripDTO.fromEntity(candidate));
            matchDTO.setCompatibilityScore(totalScore);
            matchDTO.setDestinationScore(Math.round(destinationScore * 10.0) / 10.0);
            matchDTO.setDateScore(Math.round(dateScore * 10.0) / 10.0);
            matchDTO.setInterestScore(Math.round(interestScore * 10.0) / 10.0);
            matchDTO.setBudgetScore(Math.round(budgetScore * 10.0) / 10.0);
            matchDTO.setStyleScore(Math.round(styleScore * 10.0) / 10.0);
            matchDTO.setCommonInterests(commonInterests);

            if (connOpt.isPresent()) {
                Connection conn = connOpt.get();
                matchDTO.setConnectionStatus(conn.getStatus().name());
                matchDTO.setConnectionId(conn.getId());
            } else {
                matchDTO.setConnectionStatus("NONE");
            }

            // Deduplicate by user: keep the trip with highest score
            Long userId = candidateUser.getId();
            if (!bestMatchesByUser.containsKey(userId) ||
                    bestMatchesByUser.get(userId).getCompatibilityScore() < totalScore) {
                bestMatchesByUser.put(userId, matchDTO);
            }
        }

        // Sort descending by compatibility score
        return bestMatchesByUser.values().stream()
                .sorted(Comparator.comparingDouble(MatchDTO::getCompatibilityScore).reversed())
                .collect(Collectors.toList());
    }

    public double calculateDestinationScore(String dest1, String dest2) {
        if (dest1 == null || dest2 == null) return 0.0;
        String d1 = dest1.trim().toLowerCase();
        String d2 = dest2.trim().toLowerCase();

        if (d1.equals(d2)) return 100.0;
        if (d1.contains(d2) || d2.contains(d1)) return 85.0;

        // Word token overlap
        Set<String> words1 = Arrays.stream(d1.split("[,\\s]+")).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        Set<String> words2 = Arrays.stream(d2.split("[,\\s]+")).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        for (String w : words1) {
            if (words2.contains(w) && w.length() > 2) {
                return 75.0;
            }
        }

        return 0.0;
    }

    public double calculateDateScore(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) return 0.0;

        LocalDate latestStart = start1.isAfter(start2) ? start1 : start2;
        LocalDate earliestEnd = end1.isBefore(end2) ? end1 : end2;

        if (!latestStart.isAfter(earliestEnd)) {
            // Overlapping dates
            long overlapDays = ChronoUnit.DAYS.between(latestStart, earliestEnd) + 1;
            long span1 = ChronoUnit.DAYS.between(start1, end1) + 1;
            long span2 = ChronoUnit.DAYS.between(start2, end2) + 1;
            double ratio = (double) overlapDays / (double) Math.max(1, Math.min(span1, span2));
            return Math.min(100.0, ratio * 100.0);
        }
        // A match requires at least one shared travel day.
        return 0.0;
    }

    public List<String> findCommonInterests(Trip t1, Trip t2) {
        Set<String> set1 = extractInterests(t1);
        Set<String> set2 = extractInterests(t2);

        Set<String> common = new HashSet<>(set1);
        common.retainAll(set2);
        return new ArrayList<>(common);
    }

    public double calculateInterestScore(Trip t1, Trip t2, List<String> common) {
        Set<String> set1 = extractInterests(t1);
        Set<String> set2 = extractInterests(t2);

        if (set1.isEmpty() && set2.isEmpty()) return 50.0;

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) return 50.0;
        return ((double) common.size() / (double) union.size()) * 100.0;
    }

    private Set<String> extractInterests(Trip trip) {
        Set<String> interests = new HashSet<>();
        if (trip.getInterests() != null && !trip.getInterests().isBlank()) {
            Arrays.stream(trip.getInterests().split("[,;]+"))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isEmpty())
                    .forEach(interests::add);
        }
        if (interests.isEmpty() && trip.getUser() != null && trip.getUser().getInterests() != null) {
            Arrays.stream(trip.getUser().getInterests().split("[,;]+"))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isEmpty())
                    .forEach(interests::add);
        }
        return interests;
    }

    public double calculateBudgetScore(Double b1, Double b2) {
        if (b1 == null || b2 == null) return 50.0;
        if (b1 <= 0 && b2 <= 0) return 100.0;
        double maxB = Math.max(b1, b2);
        if (maxB <= 0) return 100.0;
        double diff = Math.abs(b1 - b2);
        double similarity = 100.0 - ((diff / maxB) * 100.0);
        return Math.max(0.0, similarity);
    }

    public double calculateStyleScore(String style1, String style2) {
        if (style1 == null || style2 == null) return 50.0;
        String s1 = style1.trim().toUpperCase();
        String s2 = style2.trim().toUpperCase();

        if (s1.equals(s2)) return 100.0;

        // Compatible styles
        if ((s1.contains("BACKPACKER") || s1.contains("BUDGET")) && (s2.contains("BACKPACKER") || s2.contains("BUDGET"))) {
            return 80.0;
        }
        if ((s1.contains("SOLO") || s1.contains("ADVENTURE")) && (s2.contains("SOLO") || s2.contains("ADVENTURE"))) {
            return 80.0;
        }
        if ((s1.contains("CULTURAL") || s1.contains("LOCAL")) && (s2.contains("CULTURAL") || s2.contains("LOCAL"))) {
            return 75.0;
        }

        return 30.0;
    }
}

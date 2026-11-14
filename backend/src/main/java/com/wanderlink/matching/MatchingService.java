package com.wanderlink.matching;

import com.wanderlink.trip.Trip;
import com.wanderlink.trip.TripRepository;
import com.wanderlink.trip.TripService;
import com.wanderlink.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementing the weighted traveler compatibility algorithm.
 *
 * Matching Formula:
 *   Score = 0.30*D + 0.25*T + 0.20*I + 0.15*B + 0.10*S
 *
 * Where:
 *   D = Destination compatibility (30%)
 *   T = Travel date compatibility (25%)
 *   I = Interests similarity via Jaccard index (20%)
 *   B = Relative budget compatibility (15%)
 *   S = Travel style compatibility (10%)
 *
 * Note: Compatibility score measures travel style, itinerary, and preference alignment.
 * It is not a trust, safety, or identity verification score.
 */
@Service
@Transactional(readOnly = true)
public class MatchingService {

    private final TripService tripService;
    private final TripRepository tripRepository;

    public MatchingService(TripService tripService, TripRepository tripRepository) {
        this.tripService = tripService;
        this.tripRepository = tripRepository;
    }

    /**
     * Finds and ranks compatible travel buddies for a specific trip.
     *
     * @param tripId ID of the requester's trip
     * @return Ranked list of MatchDTO sorted by descending compatibility score
     */
    public List<MatchDTO> findMatches(Long tripId) {
        Trip baseTrip = tripService.findEntityById(tripId);
        Long requestingUserId = baseTrip.getUser().getId();
        String destination = baseTrip.getDestination().trim();

        // Retrieve other travelers heading to the same destination excluding the requester
        List<Trip> candidateTrips = tripRepository.findCandidateTrips(destination, requestingUserId);

        List<MatchDTO> matches = new ArrayList<>();

        for (Trip candidate : candidateTrips) {
            // 1. Destination Compatibility (Weight 30%)
            // Same normalized destination = 100, different = 0
            double destinationScore = calculateDestinationScore(baseTrip.getDestination(), candidate.getDestination());

            // Initial MVP filter: only evaluate same-destination candidates
            if (destinationScore == 0.0) {
                continue;
            }

            // 2. Travel Dates Compatibility (Weight 25%)
            // Measures proportional overlap of the shorter trip's duration
            double dateScore = calculateDateScore(
                    baseTrip.getStartDate(), baseTrip.getEndDate(),
                    candidate.getStartDate(), candidate.getEndDate()
            );

            // 3. Interests Similarity (Weight 20%)
            // Jaccard similarity: common interests / total unique interests * 100
            double interestScore = calculateInterestScore(baseTrip.getInterests(), candidate.getInterests());

            // 4. Budget Compatibility (Weight 15%)
            // Relative difference between budgets
            double budgetScore = calculateBudgetScore(baseTrip.getBudget(), candidate.getBudget());

            // 5. Travel Style Compatibility (Weight 10%)
            // Same style = 100, different style = 50
            double travelStyleScore = calculateTravelStyleScore(baseTrip.getTravelStyle(), candidate.getTravelStyle());

            // Calculate weighted final score
            double rawScore = (0.30 * destinationScore)
                    + (0.25 * dateScore)
                    + (0.20 * interestScore)
                    + (0.15 * budgetScore)
                    + (0.10 * travelStyleScore);

            double finalScore = round(rawScore);

            User candidateUser = candidate.getUser();
            MatchDTO match = new MatchDTO(
                    candidateUser.getId(),
                    candidateUser.getFullName(),
                    candidateUser.getLocation(),
                    candidateUser.getBio(),
                    candidate.getInterests(),
                    candidate.getTravelStyle(),
                    candidate.getDestination(),
                    candidate.getStartDate(),
                    candidate.getEndDate(),
                    candidate.getBudget(),
                    round(destinationScore),
                    round(dateScore),
                    round(interestScore),
                    round(budgetScore),
                    round(travelStyleScore),
                    finalScore
            );

            matches.add(match);
        }

        // Sort descending by overall match score
        matches.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));

        return matches;
    }

    /**
     * Destination compatibility (D):
     * Same normalized destination = 100, different = 0.
     */
    public double calculateDestinationScore(String dest1, String dest2) {
        if (dest1 == null || dest2 == null) {
            return 0.0;
        }
        return dest1.trim().equalsIgnoreCase(dest2.trim()) ? 100.0 : 0.0;
    }

    /**
     * Travel dates compatibility (T):
     * Calculates overlapping days between [start1, end1] and [start2, end2]
     * relative to the shorter trip's duration.
     * Returns 0.0 if no overlap exists.
     */
    public double calculateDateScore(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return 0.0;
        }

        LocalDate overlapStart = start1.isAfter(start2) ? start1 : start2;
        LocalDate overlapEnd = end1.isBefore(end2) ? end1 : end2;

        if (overlapEnd.isBefore(overlapStart)) {
            return 0.0; // No overlapping days
        }

        long overlapDays = ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;
        long duration1 = ChronoUnit.DAYS.between(start1, end1) + 1;
        long duration2 = ChronoUnit.DAYS.between(start2, end2) + 1;

        long shorterDuration = Math.min(duration1, duration2);
        if (shorterDuration <= 0) {
            return 0.0;
        }

        double score = ((double) overlapDays / (double) shorterDuration) * 100.0;
        return Math.min(100.0, Math.max(0.0, score));
    }

    /**
     * Interests similarity (I):
     * Jaccard Similarity = (common interests / total unique interests) * 100.
     */
    public double calculateInterestScore(String interests1, String interests2) {
        Set<String> set1 = parseInterests(interests1);
        Set<String> set2 = parseInterests(interests2);

        if (set1.isEmpty() && set2.isEmpty()) {
            return 100.0; // Both have no specific preference constraints
        }
        if (set1.isEmpty() || set2.isEmpty()) {
            return 0.0; // One specified preferences while the other specified none
        }

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 100.0;
        }

        double jaccardIndex = (double) intersection.size() / (double) union.size();
        return jaccardIndex * 100.0;
    }

    /**
     * Budget compatibility (B):
     * Compares the relative difference between two budgets.
     * Identical budgets = 100. Larger relative difference = lower score.
     */
    public double calculateBudgetScore(Double budget1, Double budget2) {
        if (budget1 == null || budget2 == null) {
            return 50.0;
        }
        if (budget1 <= 0 && budget2 <= 0) {
            return 100.0;
        }

        double maxBudget = Math.max(budget1, budget2);
        if (maxBudget == 0) {
            return 100.0;
        }

        double diff = Math.abs(budget1 - budget2);
        double relativeDiff = (diff / maxBudget) * 100.0;
        double score = 100.0 - relativeDiff;

        return Math.min(100.0, Math.max(0.0, score));
    }

    /**
     * Travel style compatibility (S):
     * Same normalized style = 100, different style = 50.
     */
    public double calculateTravelStyleScore(String style1, String style2) {
        if (style1 == null || style2 == null) {
            return 50.0;
        }
        if (style1.trim().equalsIgnoreCase(style2.trim())) {
            return 100.0;
        }
        return 50.0;
    }

    /**
     * Helper to split comma-separated interests and normalize to lowercase strings.
     */
    private Set<String> parseInterests(String interests) {
        if (interests == null || interests.trim().isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(interests.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    /**
     * Rounds a double to two decimal places.
     */
    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}

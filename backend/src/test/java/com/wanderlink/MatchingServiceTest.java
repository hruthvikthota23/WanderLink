package com.wanderlink;

import com.wanderlink.dto.MatchDTO;
import com.wanderlink.entity.Role;
import com.wanderlink.entity.Trip;
import com.wanderlink.entity.User;
import com.wanderlink.repository.ConnectionRepository;
import com.wanderlink.repository.TripRepository;
import com.wanderlink.service.MatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private MatchingService matchingService;

    private User user1;
    private User user2;
    private User user3;

    private Trip trip1; // Maya in Goa
    private Trip trip2; // Alex in Goa (High match)
    private Trip trip3; // Rohan in Manali (Low match)

    @BeforeEach
    void setUp() {
        user1 = new User("Maya Sharma", "maya@example.com", "pass", Role.TRAVELER);
        user1.setId(1L);
        user1.setInterests("Beaches, Photography, Local Food");
        user1.setTravelStyle("SOLO");

        user2 = new User("Alex Rivera", "alex@example.com", "pass", Role.TRAVELER);
        user2.setId(2L);
        user2.setInterests("Beaches, Local Food, Scuba Diving");
        user2.setTravelStyle("BACKPACKER");

        user3 = new User("Rohan Mehta", "rohan@example.com", "pass", Role.TRAVELER);
        user3.setId(3L);
        user3.setInterests("Trekking, Mountains");
        user3.setTravelStyle("ADVENTURE");

        trip1 = new Trip();
        trip1.setId(101L);
        trip1.setUser(user1);
        trip1.setDestination("Goa");
        trip1.setStartDate(LocalDate.of(2026, 12, 10));
        trip1.setEndDate(LocalDate.of(2026, 12, 16));
        trip1.setBudget(12000.0);
        trip1.setInterests("Beaches, Photography, Local Food");
        trip1.setTravelStyle("SOLO");

        trip2 = new Trip();
        trip2.setId(102L);
        trip2.setUser(user2);
        trip2.setDestination("Goa");
        trip2.setStartDate(LocalDate.of(2026, 12, 11));
        trip2.setEndDate(LocalDate.of(2026, 12, 17));
        trip2.setBudget(13000.0);
        trip2.setInterests("Beaches, Local Food");
        trip2.setTravelStyle("BACKPACKER");

        trip3 = new Trip();
        trip3.setId(103L);
        trip3.setUser(user3);
        trip3.setDestination("Manali");
        trip3.setStartDate(LocalDate.of(2026, 12, 20));
        trip3.setEndDate(LocalDate.of(2026, 12, 25));
        trip3.setBudget(15000.0);
        trip3.setInterests("Trekking, Mountains");
        trip3.setTravelStyle("ADVENTURE");
    }

    @Test
    @DisplayName("Calculate accurate matching score and rank higher compatible traveler first")
    void testFindMatchesForTrip_RankOrder() {
        when(tripRepository.findById(101L)).thenReturn(Optional.of(trip1));
        when(tripRepository.findByUserIdNot(1L)).thenReturn(List.of(trip2, trip3));
        when(connectionRepository.findBySenderIdAndReceiverId(anyLong(), anyLong())).thenReturn(Optional.empty());

        List<MatchDTO> matches = matchingService.findMatchesForTrip(101L);

        assertNotNull(matches);
        assertEquals(2, matches.size());

        // Alex should be first with high score (> 70)
        MatchDTO topMatch = matches.get(0);
        assertEquals(2L, topMatch.getTraveler().getId());
        assertEquals("Alex Rivera", topMatch.getTraveler().getFullName());
        assertTrue(topMatch.getCompatibilityScore() > 70.0, "Top match compatibility score should be > 70");
        assertEquals(100.0, topMatch.getDestinationScore());
        assertTrue(topMatch.getCommonInterests().contains("beaches"));
        assertTrue(topMatch.getCommonInterests().contains("local food"));

        // Rohan should be second with lower score
        MatchDTO secondMatch = matches.get(1);
        assertEquals(3L, secondMatch.getTraveler().getId());
        assertTrue(topMatch.getCompatibilityScore() > secondMatch.getCompatibilityScore());
    }

    @Test
    @DisplayName("Destination scoring correctly handles exact and partial matches")
    void testDestinationScoring() {
        assertEquals(100.0, matchingService.calculateDestinationScore("Goa", "Goa"));
        assertEquals(100.0, matchingService.calculateDestinationScore("goa", "GOA "));
        assertEquals(85.0, matchingService.calculateDestinationScore("Goa", "North Goa"));
        assertEquals(0.0, matchingService.calculateDestinationScore("Goa", "Manali"));
    }

    @Test
    @DisplayName("Date score calculates overlap ratio accurately")
    void testDateScoring() {
        LocalDate s1 = LocalDate.of(2026, 12, 10);
        LocalDate e1 = LocalDate.of(2026, 12, 16); // 7 days

        LocalDate s2 = LocalDate.of(2026, 12, 10);
        LocalDate e2 = LocalDate.of(2026, 12, 16); // identical

        assertEquals(100.0, matchingService.calculateDateScore(s1, e1, s2, e2));

        LocalDate s3 = LocalDate.of(2026, 12, 11);
        LocalDate e3 = LocalDate.of(2026, 12, 15); // 5 days overlap out of 7 -> ~71.4%
        double score = matchingService.calculateDateScore(s1, e1, s3, e3);
        assertTrue(score > 70.0 && score < 75.0);
    }
}

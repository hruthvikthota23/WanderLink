package com.wanderlink;

import com.wanderlink.dto.TripDTO;
import com.wanderlink.dto.TripRequest;
import com.wanderlink.entity.Role;
import com.wanderlink.entity.Trip;
import com.wanderlink.entity.User;
import com.wanderlink.exception.BadRequestException;
import com.wanderlink.repository.TripRepository;
import com.wanderlink.repository.UserRepository;
import com.wanderlink.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TripService tripService;

    private User user;
    private TripRequest validRequest;

    @BeforeEach
    void setUp() {
        user = new User("Maya Sharma", "maya@example.com", "pass", Role.TRAVELER);
        user.setId(1L);

        validRequest = new TripRequest();
        validRequest.setDestination("Goa");
        validRequest.setStartDate(LocalDate.now().plusDays(5));
        validRequest.setEndDate(LocalDate.now().plusDays(10));
        validRequest.setBudget(10000.0);
        validRequest.setInterests("Beaches, Photography");
        validRequest.setTravelStyle("SOLO");
    }

    @Test
    @DisplayName("Create trip successfully associates trip with user")
    void testCreateTrip_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Trip savedTrip = new Trip();
        savedTrip.setId(50L);
        savedTrip.setUser(user);
        savedTrip.setDestination("Goa");
        savedTrip.setStartDate(validRequest.getStartDate());
        savedTrip.setEndDate(validRequest.getEndDate());
        savedTrip.setBudget(10000.0);

        when(tripRepository.save(any(Trip.class))).thenReturn(savedTrip);

        TripDTO result = tripService.createTrip(validRequest, 1L);

        assertNotNull(result);
        assertEquals("Goa", result.getDestination());
        assertEquals(1L, result.getUserId());
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    @DisplayName("Invalid trip dates throw BadRequestException")
    void testCreateTrip_InvalidDates() {
        validRequest.setStartDate(LocalDate.now().plusDays(10));
        validRequest.setEndDate(LocalDate.now().plusDays(5)); // End before start!

        assertThrows(BadRequestException.class, () -> tripService.createTrip(validRequest, 1L));
        verify(tripRepository, never()).save(any(Trip.class));
    }
}

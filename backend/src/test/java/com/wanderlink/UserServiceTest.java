package com.wanderlink;

import com.wanderlink.dto.RegisterRequest;
import com.wanderlink.dto.UserDTO;
import com.wanderlink.entity.Role;
import com.wanderlink.entity.User;
import com.wanderlink.exception.DuplicateEmailException;
import com.wanderlink.repository.UserRepository;
import com.wanderlink.security.JwtUtil;
import com.wanderlink.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFullName("John Traveler");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("secret123");
        registerRequest.setRole(Role.TRAVELER);
    }

    @Test
    @DisplayName("Registering a new user hashes password and saves with TRAVELER role")
    void testRegister_Success() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed_secret123");

        User savedUser = new User();
        savedUser.setId(10L);
        savedUser.setFullName("John Traveler");
        savedUser.setEmail("john@example.com");
        savedUser.setRole(Role.TRAVELER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.register(registerRequest);

        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("John Traveler", result.getFullName());
        assertEquals(Role.TRAVELER, result.getRole());
        verify(passwordEncoder).encode("secret123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Registering an existing email throws DuplicateEmailException")
    void testRegister_DuplicateEmail() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}

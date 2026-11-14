package com.wanderlink.service;

import com.wanderlink.dto.AuthResponse;
import com.wanderlink.dto.LoginRequest;
import com.wanderlink.dto.RegisterRequest;
import com.wanderlink.dto.UserDTO;
import com.wanderlink.entity.Role;
import com.wanderlink.entity.User;
import com.wanderlink.exception.BadRequestException;
import com.wanderlink.exception.DuplicateEmailException;
import com.wanderlink.exception.ResourceNotFoundException;
import com.wanderlink.repository.UserRepository;
import com.wanderlink.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email is already registered: " + request.getEmail());
        }

        if (request.getConfirmPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setLocation(request.getLocation());
        user.setBio(request.getBio());
        user.setInterests(request.getInterests());
        user.setTravelStyle(request.getTravelStyle());

        // Security rule: Public registration must never assign ADMIN role
        Role assignedRole = request.getRole();
        if (assignedRole == null || assignedRole == Role.ADMIN) {
            assignedRole = Role.TRAVELER;
        }
        user.setRole(assignedRole);
        user.setAvatarUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80");

        User savedUser = userRepository.save(user);
        return UserDTO.fromEntity(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        return new AuthResponse(token, UserDTO.fromEntity(user));
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserDTO.fromEntity(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserDTO.fromEntity(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (updateDTO.getFullName() != null) user.setFullName(updateDTO.getFullName());
        if (updateDTO.getAge() != null) user.setAge(updateDTO.getAge());
        if (updateDTO.getLocation() != null) user.setLocation(updateDTO.getLocation());
        if (updateDTO.getBio() != null) user.setBio(updateDTO.getBio());
        if (updateDTO.getInterests() != null) user.setInterests(updateDTO.getInterests());
        if (updateDTO.getTravelStyle() != null) user.setTravelStyle(updateDTO.getTravelStyle());
        if (updateDTO.getAvatarUrl() != null) user.setAvatarUrl(updateDTO.getAvatarUrl());

        User updatedUser = userRepository.save(user);
        return UserDTO.fromEntity(updatedUser);
    }
}

package com.wanderlink.user;

import com.wanderlink.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User management and registration.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO.Response>> register(@Valid @RequestBody UserDTO.RegisterRequest request) {
        UserDTO.Response response = userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    /**
     * Retrieves all registered users.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO.Response>>> getAllUsers() {
        List<UserDTO.Response> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Retrieves a specific user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO.Response>> getUserById(@PathVariable Long id) {
        UserDTO.Response user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Updates an existing user's profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO.Response>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO.UpdateRequest request) {
        UserDTO.Response updated = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updated));
    }
}

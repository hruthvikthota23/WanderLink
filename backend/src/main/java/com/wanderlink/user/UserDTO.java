package com.wanderlink.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Objects for User operations.
 */
public class UserDTO {

    /**
     * Request payload for user registration.
     */
    public static class RegisterRequest {
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        private String fullName;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @Positive(message = "Age must be a positive number")
        private Integer age;

        private String location;
        private String bio;
        private String interests;
        private String travelStyle;
        private String role;

        public RegisterRequest() {}

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getInterests() {
            return interests;
        }

        public void setInterests(String interests) {
            this.interests = interests;
        }

        public String getTravelStyle() {
            return travelStyle;
        }

        public void setTravelStyle(String travelStyle) {
            this.travelStyle = travelStyle;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    /**
     * Request payload for updating a user profile.
     */
    public static class UpdateRequest {
        @Size(max = 100, message = "Full name cannot exceed 100 characters")
        private String fullName;

        @Positive(message = "Age must be a positive number")
        private Integer age;

        private String location;
        private String bio;
        private String interests;
        private String travelStyle;

        public UpdateRequest() {}

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getInterests() {
            return interests;
        }

        public void setInterests(String interests) {
            this.interests = interests;
        }

        public String getTravelStyle() {
            return travelStyle;
        }

        public void setTravelStyle(String travelStyle) {
            this.travelStyle = travelStyle;
        }
    }

    /**
     * Response representation of a User.
     * Never contains the password hash.
     */
    public static class Response {
        private Long id;
        private String fullName;
        private String email;
        private Integer age;
        private String location;
        private String bio;
        private String interests;
        private String travelStyle;
        private String role;

        public Response() {}

        public Response(Long id, String fullName, String email, Integer age, String location, String bio, String interests, String travelStyle, String role) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.age = age;
            this.location = location;
            this.bio = bio;
            this.interests = interests;
            this.travelStyle = travelStyle;
            this.role = role;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getInterests() {
            return interests;
        }

        public void setInterests(String interests) {
            this.interests = interests;
        }

        public String getTravelStyle() {
            return travelStyle;
        }

        public void setTravelStyle(String travelStyle) {
            this.travelStyle = travelStyle;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}

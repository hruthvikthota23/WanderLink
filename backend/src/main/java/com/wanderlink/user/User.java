package com.wanderlink.user;

import jakarta.persistence.*;

/**
 * User entity representing registered travelers and community hosts.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "age")
    private Integer age;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "interests", length = 255)
    private String interests;

    @Column(name = "travel_style", length = 50)
    private String travelStyle;

    @Column(name = "role", length = 50)
    private String role = "USER";

    public User() {
    }

    public User(String fullName, String email, String password, Integer age, String location, String bio, String interests, String travelStyle, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.location = location;
        this.bio = bio;
        this.interests = interests;
        this.travelStyle = travelStyle;
        this.role = (role != null && !role.trim().isEmpty()) ? role : "USER";
    }

    // Getters and Setters
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

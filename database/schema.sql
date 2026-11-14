-- ====================================================================
-- WanderLink Database Initialization Schema (MySQL 8)
-- Database: wanderlink
-- ====================================================================

CREATE DATABASE IF NOT EXISTS wanderlink
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE wanderlink;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(180) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    age INT,
    location VARCHAR(150),
    bio VARCHAR(1000),
    interests VARCHAR(500),
    travel_style VARCHAR(50),
    role VARCHAR(30) NOT NULL DEFAULT 'TRAVELER',
    avatar_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Trips Table
CREATE TABLE IF NOT EXISTS trips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    destination VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    budget DOUBLE NOT NULL,
    interests VARCHAR(500),
    travel_style VARCHAR(50),
    description VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_trip_destination (destination),
    INDEX idx_trip_dates (start_date, end_date),
    INDEX idx_trip_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Connections Table
CREATE TABLE IF NOT EXISTS connections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    message VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_sender_receiver (sender_id, receiver_id),
    INDEX idx_connection_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Accommodations Table
CREATE TABLE IF NOT EXISTS accommodations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    host_id BIGINT,
    property_name VARCHAR(200) NOT NULL,
    location VARCHAR(200) NOT NULL,
    price_per_night DOUBLE NOT NULL,
    description VARCHAR(1000),
    image_url VARCHAR(500),
    amenities VARCHAR(500),
    contact_info VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (host_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_acc_location (location),
    INDEX idx_acc_price (price_per_night)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Food Listings Table
CREATE TABLE IF NOT EXISTS food_listings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    provider_id BIGINT,
    food_name VARCHAR(200) NOT NULL,
    cuisine VARCHAR(100) NOT NULL,
    location VARCHAR(200) NOT NULL,
    price DOUBLE NOT NULL,
    description VARCHAR(1000),
    image_url VARCHAR(500),
    available_days VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (provider_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_food_location (location),
    INDEX idx_food_cuisine (cuisine)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Local Guides Table
CREATE TABLE IF NOT EXISTS guides (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    guide_name VARCHAR(150) NOT NULL,
    destination VARCHAR(150) NOT NULL,
    languages VARCHAR(250),
    experience_years INT DEFAULT 1,
    price_per_day DOUBLE NOT NULL,
    bio VARCHAR(1000),
    contact_email VARCHAR(150),
    contact_phone VARCHAR(50),
    image_url VARCHAR(500),
    rating DOUBLE DEFAULT 5.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_guide_destination (destination)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Travel Groups Table
CREATE TABLE IF NOT EXISTS travel_groups (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    creator_id BIGINT,
    name VARCHAR(200) NOT NULL,
    destination VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_group_destination (destination)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Group Members Table
CREATE TABLE IF NOT EXISTS group_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(50) DEFAULT 'MEMBER',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES travel_groups(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_group_user (group_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

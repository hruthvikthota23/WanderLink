-- ====================================================================
-- WanderLink Seed Data (MySQL 8)
-- Coherent Goa Demonstration Scenario
-- Passwords are BCrypt hashes of:
--   admin123 -> $2a$10$wT3wT8qI345hY/H0h9E.4eU7XGgH.k9M6o.VpZtT7o41N5fE4a5Gq (or standard demo bcrypt)
--   traveler123 -> $2a$10$e8R6.yG5jZ/mHqL7J1e7OeN7cT5X2vY1mR9nK4jW8lP3bV6aD9e2m
-- ====================================================================

USE wanderlink;

-- 1. Insert Users
INSERT INTO users (id, full_name, email, password, age, location, bio, interests, travel_style, role, avatar_url) VALUES
(1, 'WanderLink Admin', 'admin@wanderlink.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 32, 'Panaji, Goa', 'Platform administrator managing community trust and safety.', 'Community, Safety, Travel', 'ORGANIZER', 'ADMIN', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&auto=format&fit=crop&q=80'),
(2, 'Maya Sharma', 'maya.sharma@example.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 26, 'Mumbai, India', 'Solo backpacker & travel photographer exploring coastal heritage and local stories.', 'Beaches, Photography, Local Food, Sunset Views, Yoga', 'SOLO', 'TRAVELER', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80'),
(3, 'Alex Rivera', 'alex.rivera@example.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 28, 'Barcelona, Spain', 'Digital nomad on a journey across South Asia seeking local food and adventure.', 'Beaches, Local Food, Scuba Diving, Photography, Music', 'BACKPACKER', 'TRAVELER', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80'),
(4, 'Priya Patel', 'priya.patel@example.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 24, 'Bengaluru, India', 'Product designer traveling during long weekends. Love quiet homestays and community meetups.', 'Beaches, Cafes, Heritage, Architecture, Photography', 'BUDGET', 'TRAVELER', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80'),
(5, 'Rohan Mehta', 'rohan.mehta@example.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 29, 'Delhi, India', 'Outdoor enthusiast and trekker chasing Himalayan peaks and mountain homestays.', 'Trekking, Mountains, Wildlife, Camping', 'ADVENTURE', 'TRAVELER', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80'),
(6, 'Elena Rostova', 'elena.rostova@example.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 27, 'Berlin, Germany', 'Cultural anthropologist exploring authentic village life and artisanal traditions.', 'Art, Local Culture, Vegetarian Food, Cycling', 'CULTURAL', 'TRAVELER', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&auto=format&fit=crop&q=80'),
(7, 'Maria D''Souza', 'maria.souza@example.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 48, 'Anjuna, Goa', 'Homestay owner and passionate home cook sharing authentic Goan recipes.', 'Cooking, Homestay Hosting, Gardening', 'LOCAL', 'HOST', 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&auto=format&fit=crop&q=80'),
(8, 'Savio Fernandes', 'savio.fernandes@example.com', '$2a$10$y6iG97pBqS/nF8lB7Yg0yeK5X2pE1mR9nK4jW8lP3bV6aD9e2m012', 34, 'Panaji, Goa', 'Certified local guide showcasing hidden Goan backwaters and heritage quarters.', 'History, Kayaking, Nature Trails, Storytelling', 'GUIDED', 'GUIDE', 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&auto=format&fit=crop&q=80')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name);

-- 2. Insert Trips (Coherent Goa demonstration matches)
INSERT INTO trips (id, user_id, destination, start_date, end_date, budget, interests, travel_style, description) VALUES
(1, 2, 'Goa', '2026-12-10', '2026-12-16', 12000.0, 'Beaches, Photography, Local Food, Sunsets', 'SOLO', 'Solo trip to Goa exploring Anjuna cliffs, Fontainhas heritage quarters, and local seafood shacks.'),
(2, 3, 'Goa', '2026-12-11', '2026-12-17', 13500.0, 'Beaches, Local Food, Photography, Music', 'BACKPACKER', 'Heading to Goa for coastal remote work and beach exploration. Looking to split scooter rental and meals.'),
(3, 4, 'Goa', '2026-12-12', '2026-12-15', 10000.0, 'Beaches, Cafes, Heritage, Architecture', 'BUDGET', 'Short budget trip to Goa focusing on Portuguese architecture in Panaji and cafe hopping.'),
(4, 5, 'Manali, Himachal', '2026-12-20', '2026-12-27', 15000.0, 'Trekking, Mountains, Camping', 'ADVENTURE', 'Winter trek in Old Manali and Solang valley.'),
(5, 6, 'Jaipur, Rajasthan', '2026-12-15', '2026-12-20', 18000.0, 'Art, Architecture, Heritage, Local Food', 'CULTURAL', 'Visiting Amber Fort, Hawa Mahal, and artisan studios.')
ON DUPLICATE KEY UPDATE destination=VALUES(destination);

-- 3. Insert Accommodations
INSERT INTO accommodations (id, host_id, property_name, location, price_per_night, description, image_url, amenities, contact_info) VALUES
(1, 7, 'Sea Breeze Heritage Homestay', 'Anjuna, Goa', 1500.0, 'Charming Portuguese-era villa located 5 minutes walk from Anjuna beach with shared tropical garden and WiFi.', 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop&q=80', 'WiFi, Air Conditioning, Kitchen Access, Balcony, Bicycle Rental', '+91 98221 00112'),
(2, 7, 'Fontainhas Latin Quarter Villa', 'Panaji, Goa', 2200.0, 'Stay inside Goa''s iconic historic Latin Quarter surrounded by pastel homes and cafes.', 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600&auto=format&fit=crop&q=80', 'Heritage Decor, High-speed Fiber WiFi, Dedicated Desk', '+91 98221 00113'),
(3, 5, 'Green Valley Bamboo Cottage', 'Old Manali, Himachal', 1100.0, 'Cozy wooden and bamboo cottage overlooking snow-capped peaks and apple orchards in Old Manali.', 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600&auto=format&fit=crop&q=80', 'Mountain Views, Bonfire Pit, Homemade Siddu, High-speed WiFi', '+91 94180 55421')
ON DUPLICATE KEY UPDATE property_name=VALUES(property_name);

-- 4. Insert Food Listings
INSERT INTO food_listings (id, provider_id, food_name, cuisine, location, price, description, image_url, available_days) VALUES
(1, 7, 'Authentic Goan Fish Curry Thali', 'Goan Seafood', 'Anjuna, Goa', 250.0, 'Family-style lunch thali prepared with the morning''s fresh catch from Chapora harbor, coconut-kokum curry, kismoor relish, and sol kadi.', 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80', 'Daily Lunch (12:30 PM - 3:30 PM)'),
(2, 7, 'Pao, Poi & Morning Chai Breakfast', 'Goan Bakery & Breakfast', 'Panaji, Goa', 120.0, 'Traditional wood-fired Goan bread served with spicy mushroom xacuti, boiled eggs, and sweet freshly ground cardamom ginger chai.', 'https://images.unsplash.com/photo-1525351484163-7529414344d8?w=600&auto=format&fit=crop&q=80', 'Daily Breakfast (7:30 AM - 10:30 AM)'),
(3, 5, 'Himachali Siddu & Desi Ghee Feast', 'Himachali Traditional', 'Old Manali, Himachal', 180.0, 'Steamed local fermented wheat buns stuffed with spiced poppy seed filling, drenched in warm mountain cow ghee and coriander chutney.', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600&auto=format&fit=crop&q=80', 'Thursday to Sunday')
ON DUPLICATE KEY UPDATE food_name=VALUES(food_name);

-- 5. Insert Local Guides
INSERT INTO guides (id, user_id, guide_name, destination, languages, experience_years, price_per_day, bio, contact_email, contact_phone, image_url, rating) VALUES
(1, 8, 'Savio Fernandes', 'Goa', 'English, Konkani, Hindi, Portuguese', 8, 1200.0, 'Lifelong Goan passionate about secret waterfalls, Chorao island kayaking, and colonial heritage walking tours.', 'savio.fernandes@example.com', '+91 98221 44556', 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400&auto=format&fit=crop&q=80', 4.9),
(2, 5, 'Arjun Rao', 'Manali, Himachal', 'English, Hindi, Pahadi', 6, 1500.0, 'Certified mountaineer and wilderness guide for safe high-altitude trekking and stargazing campouts.', 'arjun.rao@example.com', '+91 98160 33221', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80', 4.8),
(3, 6, 'Sunita Sharma', 'Jaipur, Rajasthan', 'English, Hindi, French', 10, 1400.0, 'Heritage storyteller unveiling secret courtyards, artisan pottery studios, and royal sunset spots.', 'sunita.sharma@example.com', '+91 94140 88992', 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80', 5.0)
ON DUPLICATE KEY UPDATE guide_name=VALUES(guide_name);

-- 6. Insert Travel Groups
INSERT INTO travel_groups (id, creator_id, name, destination, description, image_url) VALUES
(1, 2, 'Goa Solo Travelers Club', 'Goa', 'The community hub for solo travelers, backpackers, and digital nomads visiting Goa. Meetups, co-working, and scooter road trips.', 'https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600&auto=format&fit=crop&q=80'),
(2, 5, 'Himalayan Trekkers & Trailblazers', 'Manali, Himachal', 'Connecting hikers, mountain lovers, and outdoor photographers exploring Himachal Pradesh trails.', 'https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=600&auto=format&fit=crop&q=80'),
(3, 6, 'Heritage Wanderers of India', 'Jaipur, Rajasthan', 'A collaborative circle for lovers of royal architecture, stepwells, traditional folk arts, and regional cuisines.', 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=600&auto=format&fit=crop&q=80')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 7. Insert Group Members
INSERT INTO group_members (group_id, user_id, role) VALUES
(1, 2, 'CREATOR'),
(1, 3, 'MEMBER'),
(1, 4, 'MEMBER'),
(2, 5, 'CREATOR'),
(3, 6, 'CREATOR')
ON DUPLICATE KEY UPDATE role=VALUES(role);

-- 8. Insert Demo Connection
INSERT INTO connections (id, sender_id, receiver_id, status, message) VALUES
(1, 3, 2, 'PENDING', 'Hey Maya! Saw your Goa trip on WanderLink - our dates and beach interests match up really well. Would love to connect!')
ON DUPLICATE KEY UPDATE status=VALUES(status);

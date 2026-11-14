package com.wanderlink.service;

import com.wanderlink.entity.*;
import com.wanderlink.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DataSeederService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ConnectionRepository connectionRepository;
    private final AccommodationRepository accommodationRepository;
    private final FoodListingRepository foodListingRepository;
    private final GuideRepository guideRepository;
    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${wanderlink.data.seed:true}")
    private boolean seedEnabled;

    public DataSeederService(UserRepository userRepository,
                             TripRepository tripRepository,
                             ConnectionRepository connectionRepository,
                             AccommodationRepository accommodationRepository,
                             FoodListingRepository foodListingRepository,
                             GuideRepository guideRepository,
                             TravelGroupRepository travelGroupRepository,
                             GroupMemberRepository groupMemberRepository,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.connectionRepository = connectionRepository;
        this.accommodationRepository = accommodationRepository;
        this.foodListingRepository = foodListingRepository;
        this.guideRepository = guideRepository;
        this.travelGroupRepository = travelGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) return;

        // Check if data already exists to prevent duplicate seeding across restarts
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("Seeding initial WanderLink demo data...");

        // 1. Seed Users
        User admin = createUser("WanderLink Admin", "admin@wanderlink.com", "admin123", Role.ADMIN,
                32, "Panaji, Goa", "Platform administrator managing community trust and safety.",
                "Community, Safety, Travel", "ORGANIZER",
                "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&auto=format&fit=crop&q=80");

        User maya = createUser("Maya Sharma", "maya.sharma@example.com", "traveler123", Role.TRAVELER,
                26, "Mumbai, India", "Solo backpacker & travel photographer exploring coastal heritage, sunsets, and local stories.",
                "Beaches, Photography, Local Food, Sunset Views, Yoga", "SOLO",
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80");

        User alex = createUser("Alex Rivera", "alex.rivera@example.com", "traveler123", Role.TRAVELER,
                28, "Barcelona, Spain", "Digital nomad on a journey across South Asia seeking local food, beaches, and scooter adventures.",
                "Beaches, Local Food, Scuba Diving, Photography, Music", "BACKPACKER",
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80");

        User priya = createUser("Priya Patel", "priya.patel@example.com", "traveler123", Role.TRAVELER,
                24, "Bengaluru, India", "Product designer traveling on long weekends. Love quiet homestays, cafe hopping, and heritage walks.",
                "Beaches, Cafes, Heritage, Architecture, Photography", "BUDGET",
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80");

        User rohan = createUser("Rohan Mehta", "rohan.mehta@example.com", "traveler123", Role.TRAVELER,
                29, "Delhi, India", "Outdoor enthusiast and trekker chasing Himalayan peaks and mountain homestays.",
                "Trekking, Mountains, Wildlife, Camping, Campfires", "ADVENTURE",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80");

        User elena = createUser("Elena Rostova", "elena.rostova@example.com", "traveler123", Role.TRAVELER,
                27, "Berlin, Germany", "Cultural anthropologist exploring authentic village handicrafts, architecture, and plant-based feasts.",
                "Art, Architecture, Local Culture, Vegetarian Food, Cycling", "CULTURAL",
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&auto=format&fit=crop&q=80");

        User maria = createUser("Maria D'Souza", "maria.souza@example.com", "host123", Role.HOST,
                48, "Anjuna, Goa", "Homestay owner and passionate home cook sharing authentic 4th-generation Goan recipes.",
                "Cooking, Homestay Hosting, Gardening", "LOCAL",
                "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&auto=format&fit=crop&q=80");

        User savio = createUser("Savio Fernandes", "savio.fernandes@example.com", "guide123", Role.GUIDE,
                34, "Panaji, Goa", "Certified local guide showcasing hidden Goan backwaters, waterfalls, and Portuguese heritage quarters.",
                "History, Kayaking, Nature Trails, Storytelling", "GUIDED",
                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&auto=format&fit=crop&q=80");

        // 2. Seed Coherent Goa Demonstration Scenario Trips
        createTrip(maya, "Goa", LocalDate.now().plusDays(10), LocalDate.now().plusDays(16), 12000.0,
                "Beaches, Photography, Local Food, Sunsets", "SOLO",
                "Solo trip to Goa exploring Anjuna cliffs, Fontainhas heritage quarters, and local seafood shacks. Seeking travel companions to split scooter and food tours!");

        createTrip(alex, "Goa", LocalDate.now().plusDays(11), LocalDate.now().plusDays(17), 13500.0,
                "Beaches, Local Food, Photography, Music", "BACKPACKER",
                "Heading to Goa for coastal remote work and beach exploration. Would love to connect with other travelers for sunset beach volleyball and food trips.");

        createTrip(priya, "Goa", LocalDate.now().plusDays(12), LocalDate.now().plusDays(15), 10000.0,
                "Beaches, Cafes, Heritage, Architecture", "BUDGET",
                "Short budget trip to Goa focusing on Portuguese architecture in Panaji, cafe hopping in Assagao, and peaceful morning beach walks.");

        createTrip(rohan, "Manali, Himachal", LocalDate.now().plusDays(20), LocalDate.now().plusDays(27), 15000.0,
                "Trekking, Mountains, Camping", "ADVENTURE",
                "High-altitude winter trek in Old Manali and Solang. Looking for fellow trekkers with winter gear.");

        createTrip(elena, "Jaipur, Rajasthan", LocalDate.now().plusDays(15), LocalDate.now().plusDays(20), 18000.0,
                "Art, Architecture, Heritage, Local Food", "CULTURAL",
                "Visiting Amber Fort, Hawa Mahal, and local blue pottery studios in Jaipur.");

        // 3. Seed Accommodations
        createAccommodation(maria, "Sea Breeze Heritage Homestay", "Anjuna, Goa", 1500.0,
                "Charming Portuguese-era villa located 5 minutes walk from Anjuna beach. Private veranda, shared lush tropical garden, high-speed WiFi, and home-cooked breakfasts.",
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop&q=80",
                "WiFi, Air Conditioning, Kitchen Access, Balcony, Bicycle Rental", "+91 98221 00112");

        createAccommodation(maria, "Fontainhas Latin Quarter Villa", "Panaji, Goa", 2200.0,
                "Stay inside Goa's iconic historic Latin Quarter. Surrounded by pastel-colored Portuguese homes, art galleries, and quaint cafes. Ideal for digital nomads.",
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600&auto=format&fit=crop&q=80",
                "Heritage Decor, High-speed Fiber WiFi, Dedicated Desk, Daily Housekeeping", "+91 98221 00113");

        createAccommodation(rohan, "Green Valley Bamboo Cottage", "Old Manali, Himachal", 1100.0,
                "Cozy wooden and bamboo cottage overlooking snow-capped peaks and apple orchards in Old Manali. Quiet location with bonfire facilities.",
                "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600&auto=format&fit=crop&q=80",
                "Mountain Views, Bonfire Pit, Homemade Siddu, High-speed WiFi", "+91 94180 55421");

        // 4. Seed Local Food Experiences
        createFoodListing(maria, "Authentic Goan Fish Curry Thali", "Goan Seafood", "Anjuna, Goa", 250.0,
                "Family-style lunch thali prepared with the morning's fresh catch from Chapora harbor, coconut-kokum curry, kismoor dry prawn relish, hot rice, and sol kadi.",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80",
                "Everyday (Lunch 12:30 PM - 3:30 PM)");

        createFoodListing(maria, "Pao, Poi & Morning Chai Breakfast", "Goan Bakery & Breakfast", "Panaji, Goa", 120.0,
                "Traditional wood-fired Goan bread (Poi and Undo) served with spicy mushroom xacuti, boiled eggs, and sweet freshly ground cardamom ginger chai.",
                "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=600&auto=format&fit=crop&q=80",
                "Daily (7:30 AM - 10:30 AM)");

        createFoodListing(rohan, "Himachali Siddu & Desi Ghee Feast", "Himachali Traditional", "Old Manali, Himachal", 180.0,
                "Steamed local fermented wheat buns stuffed with spiced walnut and poppy seed filling, drenched in warm mountain cow ghee and coriander mint chutney.",
                "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600&auto=format&fit=crop&q=80",
                "Thursday to Sunday (Evenings)");

        // 5. Seed Local Guides
        createGuide(savio, "Savio Fernandes", "Goa", "English, Konkani, Hindi, Portuguese", 8, 1200.0,
                "Lifelong Goan passionate about sharing our state's hidden side: secret waterfalls in the Western Ghats, Chorao island bird watching by kayak, and walking tours of colonial Latin Quarters.",
                "savio.fernandes@example.com", "+91 98221 44556",
                "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400&auto=format&fit=crop&q=80", 4.9);

        createGuide(rohan, "Arjun Rao", "Manali, Himachal", "English, Hindi, Pahadi", 6, 1500.0,
                "Certified mountaineer and wilderness guide. Leads safe, high-altitude nature walks, Solang valley treks, and stargazing campouts.",
                "arjun.rao@example.com", "+91 98160 33221",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80", 4.8);

        createGuide(elena, "Sunita Sharma", "Jaipur, Rajasthan", "English, Hindi, French", 10, 1400.0,
                "Heritage enthusiast and storyteller. Unveil secret courtyards of the walled city, artisan block-printing studios, and sunset viewpoints at Nahargarh Fort.",
                "sunita.sharma@example.com", "+91 94140 88992",
                "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80", 5.0);

        // 6. Seed Travel Groups & Group Members
        TravelGroup goaGroup = createTravelGroup(maya, "Goa Solo Travelers Club", "Goa",
                "The community hub for solo travelers, backpackers, and digital nomads visiting Goa. Join us for sunset meetups, cafe co-working, and scooter road trips.",
                "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600&auto=format&fit=crop&q=80");

        TravelGroup himachalGroup = createTravelGroup(rohan, "Himalayan Trekkers & Trailblazers", "Manali, Himachal",
                "Connecting hikers, mountain lovers, and outdoor photographers exploring Himachal Pradesh trails and passes.",
                "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=600&auto=format&fit=crop&q=80");

        TravelGroup heritageGroup = createTravelGroup(elena, "Heritage Wanderers of India", "Jaipur, Rajasthan",
                "A collaborative community for lovers of architecture, stepwells, traditional folk arts, and historic quarters across India.",
                "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=600&auto=format&fit=crop&q=80");

        // Add members to Goa group
        groupMemberRepository.save(new GroupMember(goaGroup, alex, "MEMBER"));
        groupMemberRepository.save(new GroupMember(goaGroup, priya, "MEMBER"));

        // 7. Seed one sample connection between Alex and Maya (PENDING)
        Connection demoConn = new Connection(alex, maya, "Hey Maya! Saw your Goa trip on WanderLink - our dates and beach interests match up really well. Would love to connect!");
        connectionRepository.save(demoConn);

        System.out.println("WanderLink demo dataset successfully seeded!");
    }

    private User createUser(String name, String email, String password, Role role,
                            Integer age, String location, String bio, String interests,
                            String travelStyle, String avatarUrl) {
        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setAge(age);
        user.setLocation(location);
        user.setBio(bio);
        user.setInterests(interests);
        user.setTravelStyle(travelStyle);
        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

    private Trip createTrip(User user, String destination, LocalDate start, LocalDate end,
                            Double budget, String interests, String travelStyle, String description) {
        Trip trip = new Trip();
        trip.setUser(user);
        trip.setDestination(destination);
        trip.setStartDate(start);
        trip.setEndDate(end);
        trip.setBudget(budget);
        trip.setInterests(interests);
        trip.setTravelStyle(travelStyle);
        trip.setDescription(description);
        return tripRepository.save(trip);
    }

    private Accommodation createAccommodation(User host, String name, String location,
                                              Double price, String description, String imageUrl,
                                              String amenities, String contact) {
        Accommodation acc = new Accommodation();
        acc.setHost(host);
        acc.setPropertyName(name);
        acc.setLocation(location);
        acc.setPricePerNight(price);
        acc.setDescription(description);
        acc.setImageUrl(imageUrl);
        acc.setAmenities(amenities);
        acc.setContactInfo(contact);
        return accommodationRepository.save(acc);
    }

    private FoodListing createFoodListing(User provider, String name, String cuisine,
                                          String location, Double price, String description,
                                          String imageUrl, String availableDays) {
        FoodListing food = new FoodListing();
        food.setProvider(provider);
        food.setFoodName(name);
        food.setCuisine(cuisine);
        food.setLocation(location);
        food.setPrice(price);
        food.setDescription(description);
        food.setImageUrl(imageUrl);
        food.setAvailableDays(availableDays);
        return foodListingRepository.save(food);
    }

    private Guide createGuide(User user, String name, String destination, String languages,
                              Integer experience, Double price, String bio, String email,
                              String phone, String imageUrl, Double rating) {
        Guide guide = new Guide();
        guide.setUser(user);
        guide.setGuideName(name);
        guide.setDestination(destination);
        guide.setLanguages(languages);
        guide.setExperienceYears(experience);
        guide.setPricePerDay(price);
        guide.setBio(bio);
        guide.setContactEmail(email);
        guide.setContactPhone(phone);
        guide.setImageUrl(imageUrl);
        guide.setRating(rating);
        return guideRepository.save(guide);
    }

    private TravelGroup createTravelGroup(User creator, String name, String destination,
                                          String description, String imageUrl) {
        TravelGroup group = new TravelGroup();
        group.setCreator(creator);
        group.setName(name);
        group.setDestination(destination);
        group.setDescription(description);
        group.setImageUrl(imageUrl);
        TravelGroup saved = travelGroupRepository.save(group);

        if (creator != null) {
            groupMemberRepository.save(new GroupMember(saved, creator, "CREATOR"));
        }
        return saved;
    }
}

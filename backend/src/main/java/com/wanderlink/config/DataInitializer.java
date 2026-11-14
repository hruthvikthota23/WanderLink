package com.wanderlink.config;

import com.wanderlink.accommodation.Accommodation;
import com.wanderlink.accommodation.AccommodationRepository;
import com.wanderlink.connection.Connection;
import com.wanderlink.connection.ConnectionRepository;
import com.wanderlink.food.FoodListing;
import com.wanderlink.food.FoodRepository;
import com.wanderlink.group.GroupMember;
import com.wanderlink.group.GroupMemberRepository;
import com.wanderlink.group.TravelGroup;
import com.wanderlink.group.GroupRepository;
import com.wanderlink.trip.Trip;
import com.wanderlink.trip.TripRepository;
import com.wanderlink.user.User;
import com.wanderlink.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Initializes predictable demonstration sample data for WanderLink.
 * Avoids duplicate insertion upon application restart.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ConnectionRepository connectionRepository;
    private final AccommodationRepository accommodationRepository;
    private final FoodRepository foodRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${wanderlink.data.seed:true}")
    private boolean seedEnabled;

    public DataInitializer(UserRepository userRepository,
                           TripRepository tripRepository,
                           ConnectionRepository connectionRepository,
                           AccommodationRepository accommodationRepository,
                           FoodRepository foodRepository,
                           GroupRepository groupRepository,
                           GroupMemberRepository groupMemberRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.connectionRepository = connectionRepository;
        this.accommodationRepository = accommodationRepository;
        this.foodRepository = foodRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            log.info("Sample data seeding is disabled via configuration.");
            return;
        }

        if (userRepository.count() > 0) {
            log.info("Database already contains data ({} users found). Skipping seed to prevent duplicates.", userRepository.count());
            return;
        }

        log.info("Starting WanderLink sample demo data seeding...");

        String defaultHashedPassword = passwordEncoder.encode("password123");

        // 1. Create 5+ Demo Users
        User u1 = new User("Aarav Sharma", "aarav.sharma@example.com", defaultHashedPassword, 26, "Mumbai",
                "Solo backpacker, photographer and beach lover.", "Beach, Adventure, Photography", "Budget", "USER");

        User u2 = new User("Priya Patel", "priya.patel@example.com", defaultHashedPassword, 24, "Pune",
                "Amateur photographer and adventure seeker excited to explore Goa coast.", "Beach, Adventure, Photography, Food", "Budget", "USER");

        User u3 = new User("Rohan Gupta", "rohan.gupta@example.com", defaultHashedPassword, 28, "Delhi",
                "Culture enthusiast and relaxed traveler looking for chill beach vibes and cafe hopping.", "Beach, Food, Music", "Moderate", "USER");

        User u4 = new User("Ananya Iyer", "ananya.iyer@example.com", defaultHashedPassword, 25, "Bengaluru",
                "Local host and homestay owner in South Goa. Passionate about heritage architecture.", "Yoga, Nature, Food", "Luxury", "HOST");

        User u5 = new User("Vikram Singh", "vikram.singh@example.com", defaultHashedPassword, 30, "Jaipur",
                "History buff, road tripper and culinary storyteller.", "Adventure, Trekking, Culinary", "Adventure", "PROVIDER");

        User u6 = new User("Neha Desai", "neha.desai@example.com", defaultHashedPassword, 27, "Ahmedabad",
                "Mountain hiker and mountain cafe explorer.", "Hiking, Mountains, Photography", "Budget", "USER");

        List<User> savedUsers = userRepository.saveAll(Arrays.asList(u1, u2, u3, u4, u5, u6));
        u1 = savedUsers.get(0);
        u2 = savedUsers.get(1);
        u3 = savedUsers.get(2);
        u4 = savedUsers.get(3);
        u5 = savedUsers.get(4);
        u6 = savedUsers.get(5);

        log.info("Created {} sample users.", savedUsers.size());

        // 2. Create 5+ Demo Trips (Focused on Goa showcase)
        // Trip 1: The Requester's Benchmark Trip (Aarav)
        Trip t1 = new Trip(u1, "Goa", LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 25), 10000.0,
                "Beach, Adventure, Photography", "Budget", "Looking for fellow solo travelers to explore North and South Goa.");

        // Trip 2: High Match candidate (Priya: overlapping dates Dec 21-25, similar budget 11000, identical interests + style)
        Trip t2 = new Trip(u2, "Goa", LocalDate.of(2026, 12, 21), LocalDate.of(2026, 12, 25), 11000.0,
                "Beach, Adventure, Photography, Food", "Budget", "Heading to Goa for a scenic photography retreat along the beaches.");

        // Trip 3: Medium Match candidate (Rohan: overlapping dates Dec 19-24, budget 18000, some overlapping interests, Moderate style)
        Trip t3 = new Trip(u3, "Goa", LocalDate.of(2026, 12, 19), LocalDate.of(2026, 12, 24), 18000.0,
                "Beach, Food, Music", "Moderate", "Chill vacation exploring cafes, live music, and sunsets in Vagator.");

        // Trip 4: Lower Match candidate (Ananya: dates Dec 23-28, higher budget 35000, Luxury style)
        Trip t4 = new Trip(u4, "Goa", LocalDate.of(2026, 12, 23), LocalDate.of(2026, 12, 28), 35000.0,
                "Yoga, Nature, Food", "Luxury", "Wellness and architecture appreciation trip.");

        // Trip 5: Different dates (Vikram: Dec 28 to Jan 02 - zero date overlap with Aarav's trip)
        Trip t5 = new Trip(u5, "Goa", LocalDate.of(2026, 12, 28), LocalDate.of(2027, 1, 2), 12000.0,
                "Adventure, Trekking", "Adventure", "New Year exploration of Dudhsagar and spice farms.");

        // Trip 6: Different destination (Neha: Manali - verifies destination filtering)
        Trip t6 = new Trip(u6, "Manali", LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 25), 15000.0,
                "Hiking, Mountains, Photography", "Budget", "Snow trail trekking in Solang Valley.");

        tripRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6));
        log.info("Created 6 sample trips.");

        // 3. Create Sample Connection Request
        Connection c1 = new Connection(u1, u2);
        connectionRepository.save(c1);
        log.info("Created 1 pending sample connection request between Aarav and Priya.");

        // 4. Create 3+ Local Accommodation Listings
        Accommodation a1 = new Accommodation(
                u4,
                "Cozy Coconut Grove Villa near Palolem",
                "Palolem, South Goa",
                "Quiet, authentic Portuguese-style heritage cottage surrounded by palm trees, 5 minutes walk from Palolem beach.",
                1800.0,
                "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?auto=format&fit=crop&w=800&q=80"
        );

        Accommodation a2 = new Accommodation(
                u4,
                "Backpacker Haven & Garden Studio",
                "Anjuna, North Goa",
                "Vibrant garden studio with co-working desks, fiber internet, and shared rooftop terrace for travelers.",
                950.0,
                "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=800&q=80"
        );

        Accommodation a3 = new Accommodation(
                u1,
                "Cliffside Sunset Eco-Cottage",
                "Vagator, North Goa",
                "Eco-friendly wooden cottage overlooking Ozran beach cliffs. Perfect for digital nomads and sunset lovers.",
                2200.0,
                "https://images.unsplash.com/photo-1499793983690-e29da59ef1c2?auto=format&fit=crop&w=800&q=80"
        );

        accommodationRepository.saveAll(Arrays.asList(a1, a2, a3));
        log.info("Created 3 sample accommodation listings.");

        // 5. Create 3+ Homemade Food Listings
        FoodListing f1 = new FoodListing(
                u5,
                "Traditional Goan Fish Thali & Sol Kadhi Feast",
                "Goan Coastal",
                "Panaji, Goa",
                350.0,
                "Authentic homemade Kingfish curry, prawn rawa fry, kismoor, and refreshing sol kadhi prepared with grandmother's recipe.",
                "https://images.unsplash.com/photo-1610057099443-fde8c4d50f91?auto=format&fit=crop&w=800&q=80"
        );

        FoodListing f2 = new FoodListing(
                u5,
                "Home-baked Bebinca & Portuguese Tea Tasting",
                "Goan Portuguese Bakery",
                "Fontainhas, Panaji, Goa",
                200.0,
                "7-layer authentic coconut bebinca served warm with artisanal spiced tea in a historic Portuguese courtyard.",
                "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=800&q=80"
        );

        FoodListing f3 = new FoodListing(
                u2,
                "Sunset Beachside Vegan Kokum Curry & Rice",
                "Konkani Vegan",
                "Morjim, Goa",
                280.0,
                "Healthy and zesty homemade coastal curry cooked with fresh coconut milk, kokum, and organic red rice.",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=800&q=80"
        );

        foodRepository.saveAll(Arrays.asList(f1, f2, f3));
        log.info("Created 3 sample food experience listings.");

        // 6. Create 3+ Travel Groups and Membership
        TravelGroup g1 = new TravelGroup("Goa Sunset & Beach Trail Explorers", "Goa",
                "Group for solo travelers in Goa seeking photography walks, hidden beach discoveries, and sunset music jams.", u1);
        g1.setMemberCount(2);
        TravelGroup savedG1 = groupRepository.save(g1);
        groupMemberRepository.save(new GroupMember(savedG1, u1));
        groupMemberRepository.save(new GroupMember(savedG1, u2));

        TravelGroup g2 = new TravelGroup("Goan Foodie & Heritage Cafe Hoppers", "Goa",
                "Exploring historic Panaji bakeries, spice plantations, and offbeat local coastal shacks together.", u3);
        g2.setMemberCount(2);
        TravelGroup savedG2 = groupRepository.save(g2);
        groupMemberRepository.save(new GroupMember(savedG2, u3));
        groupMemberRepository.save(new GroupMember(savedG2, u4));

        TravelGroup g3 = new TravelGroup("Dudhsagar Waterfall Trek & Camp", "Goa",
                "Weekend adventure trek to the magnificent Dudhsagar waterfalls on the Goa-Karnataka border.", u5);
        g3.setMemberCount(1);
        TravelGroup savedG3 = groupRepository.save(g3);
        groupMemberRepository.save(new GroupMember(savedG3, u5));

        log.info("Created 3 sample travel groups with memberships.");
        log.info("WanderLink sample demo data seeding completed successfully.");
    }
}

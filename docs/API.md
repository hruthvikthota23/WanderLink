# WanderLink API Reference

Base URL: `http://localhost:8080/api`

All protected endpoints require the header:
```
Authorization: Bearer <JWT_TOKEN>
```

---

## Authentication

### POST `/auth/register`
Register a new user. Allowed roles: `TRAVELER`, `HOST`, `GUIDE` (not `ADMIN`).

**Request body:**
```json
{
  "fullName": "Maya Sharma",
  "email": "maya@example.com",
  "password": "secret123",
  "age": 27,
  "location": "Mumbai",
  "bio": "Solo traveler and food lover",
  "interests": ["hiking", "photography", "food"],
  "travelStyle": "BUDGET",
  "role": "TRAVELER"
}
```

**Response `201`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "fullName": "Maya Sharma",
    "email": "maya@example.com",
    "role": "TRAVELER",
    "location": "Mumbai",
    "bio": "...",
    "interests": ["hiking","photography","food"],
    "travelStyle": "BUDGET",
    "createdAt": "2024-01-01T10:00:00"
  }
}
```

---

### POST `/auth/login`
Authenticate and receive a JWT token.

**Request body:**
```json
{
  "email": "maya@example.com",
  "password": "secret123"
}
```

**Response `200`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": { ... }
}
```

---

### GET `/auth/me`
🔒 Protected. Returns the currently authenticated user's profile.

**Response `200`:** `UserDTO` object (same shape as above, without token).

---

## Users

### GET `/users`
🔒 Protected. Returns a list of all users (excluding password).

**Response `200`:** Array of `UserDTO`.

---

### GET `/users/{id}`
🔒 Protected. Returns a single user by ID.

**Response `200`:** `UserDTO`
**Response `404`:** User not found.

---

### PUT `/users/{id}`
🔒 Protected. Update the authenticated user's own profile.

**Request body (all fields optional):**
```json
{
  "fullName": "Maya S.",
  "location": "Goa",
  "bio": "Updated bio",
  "age": 28,
  "interests": ["trekking", "yoga"],
  "travelStyle": "ADVENTURE"
}
```

**Response `200`:** Updated `UserDTO`.
**Response `403`:** Cannot update another user's profile.

---

## Trips

### POST `/trips`
🔒 Protected. Create a new trip.

**Request body:**
```json
{
  "destination": "Goa",
  "startDate": "2024-12-01",
  "endDate": "2024-12-07",
  "budget": 15000.00,
  "interests": ["beach", "nightlife", "food"],
  "travelStyle": "BUDGET",
  "description": "Looking for fun travel companions for a week in Goa"
}
```

**Response `201`:** `TripDTO`

---

### GET `/trips`
Public. Returns all trips.

**Response `200`:** Array of `TripDTO`.

---

### GET `/trips/my`
🔒 Protected. Returns trips created by the authenticated user.

**Response `200`:** Array of `TripDTO`.

---

### GET `/trips/{id}`
Public. Returns a single trip by ID.

**Response `200`:** `TripDTO`
**Response `404`:** Trip not found.

---

### PUT `/trips/{id}`
🔒 Protected. Update a trip (owner only).

**Request body:** Same as create (partial updates allowed).
**Response `200`:** Updated `TripDTO`.
**Response `403`:** Not the trip owner.

---

### DELETE `/trips/{id}`
🔒 Protected. Delete a trip (owner only).

**Response `204`:** No content.
**Response `403`:** Not the trip owner.

---

## Buddy Matching

### GET `/matches?tripId={id}`
🔒 Protected. Returns compatible travelers for a given trip, sorted by match score descending.

**Query param:** `tripId` — the ID of a trip belonging to the authenticated user.

**Response `200`:**
```json
[
  {
    "userId": 3,
    "fullName": "Alex Rivera",
    "location": "Delhi",
    "bio": "Adventure seeker",
    "interests": ["hiking", "beach"],
    "travelStyle": "ADVENTURE",
    "profileImage": null,
    "tripId": 2,
    "tripDestination": "Goa",
    "tripStartDate": "2024-12-01",
    "tripEndDate": "2024-12-06",
    "tripBudget": 12000.00,
    "overallScore": 87.5,
    "destinationScore": 100.0,
    "dateScore": 83.3,
    "interestScore": 66.7,
    "budgetScore": 90.0,
    "styleScore": 50.0,
    "commonInterests": ["beach"],
    "connectionStatus": "NONE"
  }
]
```

---

## Connections

### POST `/connections`
🔒 Protected. Send a connection request to another traveler.

**Request body:**
```json
{
  "receiverId": 3
}
```

**Response `201`:** `ConnectionDTO`
**Response `400`:** Cannot connect to yourself or duplicate request.

---

### GET `/connections`
🔒 Protected. Returns all connections (sent + received) for the authenticated user.

**Response `200`:** Array of `ConnectionDTO`.

---

### PUT `/connections/{id}/accept`
🔒 Protected. Accept a pending connection request (receiver only).

**Response `200`:** Updated `ConnectionDTO` with `status: "ACCEPTED"`.
**Response `403`:** Not the receiver.

---

### PUT `/connections/{id}/reject`
🔒 Protected. Reject a pending connection request (receiver only).

**Response `200`:** Updated `ConnectionDTO` with `status: "REJECTED"`.

---

## Accommodation

### GET `/accommodations`
Public. Returns all accommodation listings.

**Response `200`:** Array of `AccommodationDTO`.

---

### GET `/accommodations/{id}`
Public. Returns a single listing.

---

### POST `/accommodations`
🔒 Protected (HOST role). Create a new listing.

**Request body:**
```json
{
  "name": "Sunrise Beach House",
  "location": "Goa",
  "pricePerNight": 1500.00,
  "description": "Cozy beach house near Baga",
  "imageUrl": "https://...",
  "amenities": "WiFi, AC, Kitchen",
  "contactInfo": "9876543210"
}
```

**Response `201`:** `AccommodationDTO`

---

### PUT `/accommodations/{id}`
🔒 Protected. Update listing (owner only).

### DELETE `/accommodations/{id}`
🔒 Protected. Delete listing (owner only).

---

## Food Experiences

### GET `/food`
Public. Returns all food listings.

### GET `/food/{id}`
Public.

### POST `/food`
🔒 Protected. Create a food listing.

**Request body:**
```json
{
  "name": "Home-Cooked Goan Thali",
  "cuisine": "Goan",
  "providerName": "Maria Souza",
  "location": "Panaji, Goa",
  "pricePerPerson": 350.00,
  "description": "Authentic Goan thali with fish curry, rice, and sol kadhi",
  "imageUrl": "https://..."
}
```

### PUT `/food/{id}` / DELETE `/food/{id}`
🔒 Protected.

---

## Local Guides

### GET `/guides`
Public. Returns all guides.

### GET `/guides/{id}`
Public.

### POST `/guides`
🔒 Protected. Create a guide profile.

**Request body:**
```json
{
  "name": "Savio Fernandes",
  "destination": "Goa",
  "languages": "English, Hindi, Konkani",
  "experienceYears": 7,
  "pricePerDay": 2500.00,
  "description": "Expert guide for beaches, heritage, and local markets",
  "contactInfo": "savio@example.com"
}
```

### PUT `/guides/{id}` / DELETE `/guides/{id}`
🔒 Protected.

---

## Travel Groups

### GET `/groups`
Public. Returns all travel groups.

### GET `/groups/{id}`
Public.

### POST `/groups`
🔒 Protected. Create a new group.

**Request body:**
```json
{
  "name": "Goa Backpackers",
  "destination": "Goa",
  "description": "Budget backpackers exploring Goa together"
}
```

### POST `/groups/{id}/join`
🔒 Protected. Join a travel group.

**Response `200`:** `GroupMemberDTO`
**Response `400`:** Already a member.

### DELETE `/groups/{id}/leave`
🔒 Protected. Leave a travel group.

**Response `204`:** No content.

### GET `/groups/{id}/members`
🔒 Protected. List all members of a group.

**Response `200`:** Array of `GroupMemberDTO`.

---

## AI Assistant

### POST `/ai/assistant`
🔒 Protected. Send a message to the AI travel assistant.

**Request body:**
```json
{
  "message": "Plan a 5-day trip to Goa on a budget of ₹15,000"
}
```

**Response `200`:**
```json
{
  "response": "Here's your 5-day Goa itinerary...",
  "suggestions": [
    "Plan a 5-day Goa trip",
    "Suggest budget-friendly destinations",
    "Find local food experiences"
  ]
}
```

---

## Common Error Responses

| Status | Meaning |
|--------|---------|
| `400 Bad Request` | Invalid input / validation failure |
| `401 Unauthorized` | Missing or invalid JWT token |
| `403 Forbidden` | Authenticated but not authorized |
| `404 Not Found` | Resource does not exist |
| `409 Conflict` | Duplicate resource (e.g., email already registered) |
| `500 Internal Server Error` | Unexpected server error |

**Error body shape:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Trip not found with id: 99",
  "timestamp": "2024-01-01T10:00:00"
}
```

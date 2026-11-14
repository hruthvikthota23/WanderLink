# 🌍 WanderLink

> **Every Journey Starts With a Connection.**

WanderLink is an AI-powered, community-based social travel platform that connects solo travelers with compatible travel companions, local homestay hosts, food experience providers, and local guides — all in one integrated application.

---

## ✨ Features

| Module | Features |
|--------|----------|
| 🔐 **Auth** | Register, login with JWT, role-based access (TRAVELER / HOST / GUIDE / ADMIN) |
| 👤 **Profile** | Full profile with interests, travel style, bio, location |
| 🗺️ **Trips** | Create, view, edit and delete trips with destination, dates, budget and style |
| 🤝 **Buddy Matching** | Weighted 5-factor compatibility algorithm (destination, dates, interests, budget, style) |
| 🔗 **Connections** | Send, accept, reject connection requests between travelers |
| 🏠 **Accommodation** | Browse local homestay and accommodation listings |
| 🍜 **Food Experiences** | Discover authentic local food experiences |
| 🧭 **Local Guides** | Find experienced local guides by destination |
| 👥 **Travel Groups** | Create, join and leave travel interest groups |
| 🤖 **AI Assistant** | OpenAI-powered travel assistant that responds to the user's question and recent chat context |
| 🌐 **Community Explore** | Unified discovery page for all community offerings |

---

## 🛠️ Technology Stack

### Frontend
- **React 18** + **Vite** — fast development and production builds
- **Tailwind CSS** — utility-first responsive styling (deep green + warm orange theme)
- **React Router DOM v6** — client-side routing with protected routes
- **Axios** — HTTP client with JWT interceptor and 401 auto-logout
- **Lucide React** — consistent icon library
- **React Context API** — global auth state and toast notifications

### Backend
- **Java 21** + **Spring Boot 3.2.5**
- **Spring Security** + **JWT (JJWT 0.12.5)** — stateless authentication
- **Spring Data JPA** + **Hibernate** — ORM with MySQL 8
- **BCrypt** — password hashing
- **Jakarta Validation** — request validation
- **Maven** — dependency management and build

### Database
- **MySQL 8** — relational database
- 8 tables: `users`, `trips`, `connections`, `accommodations`, `food_listings`, `guides`, `travel_groups`, `group_members`
- Full FK relationships, unique constraints, indexes

### AI
- Built-in **rule-based assistant** (no external API key required)
- Extensible to **OpenAI / Gemini** via environment variable

---

## 📋 Prerequisites

| Tool | Version |
|------|---------|
| Java | 21+ |
| Maven | 3.8+ |
| Node.js | 18+ |
| npm | 9+ |
| MySQL | 8.0+ |

---

## 🗄️ Database Setup

```sql
-- Connect to MySQL as root (or a user with CREATE DATABASE privilege)
CREATE DATABASE wanderlink CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- (Optional) Create a dedicated user
CREATE USER 'wanderlink'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON wanderlink.* TO 'wanderlink'@'localhost';
FLUSH PRIVILEGES;
```

> **Note:** Hibernate (`spring.jpa.hibernate.ddl-auto=update`) will auto-create all tables on first boot.
> The `DataSeederService` will seed 8 demo users, 5 trips, 3 accommodations, 3 food listings, 3 guides and 3 groups automatically.
> You can also run `database/schema.sql` and `database/seed.sql` manually if preferred.

---

## ⚙️ Backend Setup

### 1. Configure environment

```bash
cd backend
cp .env.example .env
```

Edit `.env`:

```env
DB_URL=jdbc:mysql://localhost:3306/wanderlink?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=yourpassword
JWT_SECRET=WanderLinkSuperSecretKeyForJWTTokenGeneration2024!ChangeMe
JWT_EXPIRATION_MS=86400000
WANDERLINK_SEED_DATA=true
```

### 2. Build and run

```bash
# From the WanderLink/ root directory
cd backend

# Build (skipping tests for quick start)
mvn clean package -DskipTests

# Run
mvn spring-boot:run
```

Backend starts on **http://localhost:8080**

### 3. Run tests

```bash
mvn test
```

Tests use an **H2 in-memory database** (no MySQL needed for tests).

---

## 💻 Frontend Setup

### 1. Install dependencies

```bash
cd frontend
npm install
```

### 2. Configure environment (optional)

```bash
cp .env.example .env
```

The Vite dev server proxies `/api` → `http://localhost:8080` automatically — no extra config needed for local development.

### 3. Run development server

```bash
npm run dev
```

Frontend starts on **http://localhost:5173**

### 4. Build for production

```bash
npm run build
# Output in frontend/dist/
```

---

## 🔑 Demo Credentials

Seeded automatically on first backend run:

| Email | Password | Role |
|-------|----------|------|
| admin@wanderlink.com | admin123 | ADMIN |
| maya.sharma@example.com | traveler123 | TRAVELER |
| alex.rivera@example.com | traveler123 | TRAVELER |
| priya.patel@example.com | traveler123 | TRAVELER |
| rohan.mehta@example.com | traveler123 | TRAVELER |
| elena.rostova@example.com | traveler123 | TRAVELER |
| maria.souza@example.com | host123 | HOST |
| savio.fernandes@example.com | guide123 | GUIDE |

> The **Login** page includes one-click demo credential fill buttons for convenience.

---

## 🌐 API Overview

Base URL: `http://localhost:8080/api`

| Category | Endpoints |
|----------|-----------|
| Auth | POST `/auth/register`, POST `/auth/login`, GET `/auth/me` |
| Users | GET `/users`, GET `/users/{id}`, PUT `/users/{id}` |
| Trips | GET/POST `/trips`, GET/PUT/DELETE `/trips/{id}`, GET `/trips/my` |
| Matching | GET `/matches?tripId={id}` |
| Connections | POST `/connections`, PUT `/connections/{id}/accept`, PUT `/connections/{id}/reject`, GET `/connections` |
| Accommodation | GET/POST `/accommodations`, GET/PUT/DELETE `/accommodations/{id}` |
| Food | GET/POST `/food`, GET/PUT/DELETE `/food/{id}` |
| Guides | GET/POST `/guides`, GET/PUT/DELETE `/guides/{id}` |
| Groups | GET/POST `/groups`, GET `/groups/{id}`, POST `/groups/{id}/join`, DELETE `/groups/{id}/leave`, GET `/groups/{id}/members` |
| AI | POST `/ai/assistant` |

Full API documentation: [docs/API.md](docs/API.md)

---

## 🤖 AI Assistant

The AI Travel Assistant uses OpenAI's Responses API. Set an API key in the same PowerShell window before starting the backend:

```powershell
cd backend
$env:WANDERLINK_AI_API_KEY = "your OpenAI API key"
$env:WANDERLINK_AI_MODEL = "gpt-6-luna"
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
```

The key stays on the backend and is never sent to the browser. The assistant receives the current question and up to 12 recent chat messages, and returns a reply plus structured itinerary and recommendations when relevant. An API key and API account access are required for AI answers.

---

## 🧮 Buddy Matching Algorithm

Match scores are computed server-side using a weighted formula:

```
Score = 0.30 × Destination + 0.25 × DateOverlap + 0.20 × SharedInterests + 0.15 × BudgetProximity + 0.10 × TravelStyle
```

| Factor | Weight | Description |
|--------|--------|-------------|
| Destination | 30% | Same destination = 100, different = 0 |
| Date Overlap | 25% | Overlap ratio of trip date ranges |
| Interests | 20% | Jaccard similarity of interest tags |
| Budget | 15% | Proximity score based on budget difference |
| Travel Style | 10% | Same style = 100, different = 0 |

---

## 📁 Project Structure

```
WanderLink/
├── backend/                    # Spring Boot application
│   ├── src/main/java/com/wanderlink/
│   │   ├── config/             # Security, CORS config
│   │   ├── controller/         # REST controllers
│   │   ├── dto/                # Data transfer objects
│   │   ├── entity/             # JPA entities
│   │   ├── exception/          # Custom exceptions + global handler
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── security/           # JWT filter, UserDetails
│   │   └── service/            # Business logic
│   ├── src/test/               # JUnit 5 + Mockito tests
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/                   # React + Vite application
│   ├── src/
│   │   ├── components/         # Reusable UI components
│   │   ├── context/            # AuthContext, ToastContext
│   │   ├── pages/              # Route-level page components
│   │   └── services/           # Axios API service modules
│   ├── index.html
│   ├── vite.config.js
│   ├── tailwind.config.js
│   └── package.json
├── database/
│   ├── schema.sql              # MySQL DDL
│   └── seed.sql                # Demo data
├── docs/
│   ├── API.md                  # Full API reference
│   └── SETUP.md                # Detailed setup guide
├── .gitignore
└── README.md
```

---

## ⚠️ Known Limitations

- **AI is rule-based** — no real LLM integration by default (key required for live AI)
- **No real-time messaging** — connections are async request/accept only
- **No image uploads** — listing images use external URLs
- **Email is not sent** — no SMTP integration; notifications are UI-only
- **Single server** — monolithic architecture; not horizontally scaled
- **ADMIN role** — cannot be assigned via public registration; seed admin or use DB directly

---

## 📄 License

Built for hackathon demonstration. All content including sample data is fictional.

---

## 🙏 Acknowledgements

Built with Spring Boot, React, Tailwind CSS, JJWT, Lucide React, and ❤️.

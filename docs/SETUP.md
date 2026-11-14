# WanderLink – Setup Guide

This guide walks you through setting up WanderLink from scratch on a development machine.

---

## Prerequisites

Install the following before starting:

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 21+ | https://adoptium.net |
| Apache Maven | 3.8+ | https://maven.apache.org/download.cgi |
| Node.js | 18+ | https://nodejs.org |
| MySQL Server | 8.0+ | https://dev.mysql.com/downloads/mysql/ |
| Git | Any | https://git-scm.com |

Verify installations:
```bash
java -version
mvn -version
node -v
npm -v
mysql --version
```

---

## Step 1 – Clone / Open the Project

If using Git:
```bash
git clone <your-repo-url>
cd WanderLink
```

Or open the extracted `WanderLink` folder directly.

---

## Step 2 – MySQL Database Setup

### 2a. Start MySQL server
On Windows: open **MySQL Workbench** or start the MySQL service from Services.

On macOS/Linux:
```bash
sudo service mysql start
# or
brew services start mysql
```

### 2b. Create the database
Connect as root (or your MySQL admin user):
```sql
CREATE DATABASE wanderlink
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

### 2c. (Optional) Create a dedicated database user
```sql
CREATE USER 'wanderlink_user'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON wanderlink.* TO 'wanderlink_user'@'localhost';
FLUSH PRIVILEGES;
```

> **Tables are created automatically** by Hibernate on first boot (`ddl-auto=update`).
> You do NOT need to run `schema.sql` manually, but you may if you prefer explicit DDL control.

---

## Step 3 – Backend Configuration

```bash
cd backend
cp .env.example .env
```

Edit `backend/.env` and set your values:

```env
DB_URL=jdbc:mysql://localhost:3306/wanderlink?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=WanderLinkSuperSecretKeyForJWTTokenGeneration2024!ChangeMe
JWT_EXPIRATION_MS=86400000
WANDERLINK_SEED_DATA=true
```

> ⚠️ Change `JWT_SECRET` to a long random string before deploying anywhere public.

Alternatively, you can edit `src/main/resources/application.properties` directly.

---

## Step 4 – Run the Backend

```bash
cd backend

# Option A: Run directly with Maven
mvn spring-boot:run

# Option B: Build JAR first, then run
mvn clean package -DskipTests
java -jar target/wanderlink-*.jar
```

Expected output on success:
```
Tomcat started on port(s): 8080 (http)
Started WanderLinkApplication in X.XXX seconds
[DataSeederService] Seeding demo data...
[DataSeederService] Demo data seeded successfully.
```

The backend is ready at: **http://localhost:8080**

---

## Step 5 – Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Expected output:
```
  VITE v5.x.x  ready in xxx ms
  ➜  Local:   http://localhost:5173/
```

Open **http://localhost:5173** in your browser.

> The Vite dev server automatically proxies `/api` requests to `http://localhost:8080`.
> Both servers must be running simultaneously for the app to work.

---

## Step 6 – Verify Everything Works

1. Open **http://localhost:5173**
2. You should see the WanderLink landing page
3. Click **Login** and use a demo credential:
   - Email: `maya.sharma@example.com`
   - Password: `traveler123`
4. You should be redirected to the dashboard
5. Navigate to **My Trips → Create Trip**, fill the form, then view **Buddy Matches**

---

## Running Tests

Backend tests use **H2 in-memory database** (no MySQL needed):

```bash
cd backend
mvn test
```

Test classes:
- `WanderLinkApplicationTests` — context loads with H2
- `MatchingServiceTest` — scoring algorithm unit tests
- `UserServiceTest` — registration and login logic
- `TripServiceTest` — trip CRUD and ownership
- `AIServiceTest` — rule-based assistant responses

---

## Production Build

### Frontend
```bash
cd frontend
npm run build
# Output: frontend/dist/
```

Serve `frontend/dist/` from your web server (Nginx, Apache, Vercel, Netlify, etc.).

### Backend
```bash
cd backend
mvn clean package
# Output: backend/target/wanderlink-0.0.1-SNAPSHOT.jar
java -jar backend/target/wanderlink-0.0.1-SNAPSHOT.jar
```

---

## Running with Docker (Optional)

A basic `docker-compose.yml` for local development:

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: wanderlink
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      DB_URL: jdbc:mysql://mysql:3306/wanderlink?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      DB_USERNAME: root
      DB_PASSWORD: rootpassword
      JWT_SECRET: WanderLinkSuperSecretKeyForJWTTokenGeneration2024
      WANDERLINK_SEED_DATA: "true"
    depends_on:
      - mysql

volumes:
  mysql_data:
```

```bash
docker compose up
```

---

## Troubleshooting

### "Access denied for user 'root'@'localhost'"
- Verify MySQL username and password in `.env`
- Ensure MySQL server is running

### "Could not create connection to database server"
- Check `DB_URL` — confirm port (usually 3306)
- Try adding `&allowPublicKeyRetrieval=true` to `DB_URL`

### Frontend shows blank or "Network Error"
- Make sure the backend is running on port 8080
- Check the Vite proxy in `frontend/vite.config.js`

### "Port 8080 already in use"
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -i :8080
kill -9 <PID>
```

### "CORS error" in browser
- Ensure you're accessing the frontend via `http://localhost:5173` (not a different port)
- Check `CorsConfig.java` — allowed origins include `localhost:5173` and `localhost:3000`

### Demo data not seeded
- Ensure `WANDERLINK_SEED_DATA=true` in `.env`
- If the database already has users (e.g., from a previous run), the seeder skips to avoid duplicates
- To re-seed: `DROP DATABASE wanderlink; CREATE DATABASE wanderlink;` and restart backend

---

## Environment Variables Reference

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | *(required)* | JDBC URL for MySQL |
| `DB_USERNAME` | *(required)* | MySQL username |
| `DB_PASSWORD` | *(required)* | MySQL password |
| `JWT_SECRET` | *(required)* | Secret key for JWT signing |
| `JWT_EXPIRATION_MS` | `86400000` | JWT token TTL (1 day) |
| `WANDERLINK_SEED_DATA` | `true` | Auto-seed demo data on boot |
| `WANDERLINK_AI_API_KEY` | *(empty)* | External AI API key (optional) |
| `WANDERLINK_AI_PROVIDER` | `builtin-rule-engine` | AI provider (`builtin-rule-engine`, `openai`, `gemini`) |
| `SERVER_PORT` | `8080` | Backend HTTP port |

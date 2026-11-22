# Gautam Kumar Singh — Portfolio (Full Stack)

A single-container portfolio: a **Spring Boot 3** application serves the original HTML/CSS/JS frontend **and** exposes the REST API that the "Ping API" and contact form now call for real.

## Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 17, Spring Boot 3.3, Spring Web MVC, Bean Validation, Spring Data JPA |
| Database | Embedded H2 (self-contained, no external service needed) |
| Frontend | The original Tailwind + Lucide single-page HTML, served from `static/` |
| Build | Maven, multi-stage Dockerfile |
| CI | GitHub Actions |
| Hosting | Render (Docker web service) |

## API

Base path: `/api/v1/gautam`

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/profile` | Returns the engineer profile used by the hero widget |
| `POST` | `/contact` | Validates and stores a contact inquiry |
| `GET` | `/health` | Simple liveness payload |
| `GET` | `/actuator/health` | Spring Boot health check (used by Render) |

Example:

```bash
curl http://localhost:8080/api/v1/gautam/profile

curl -X POST http://localhost:8080/api/v1/gautam/contact \
  -H 'Content-Type: application/json' \
  -d '{"name":"Recruiter","email":"hr@example.com","subject":"Backend Role","message":"Hi Gautam!"}'
```

Validation errors return HTTP 400 with `{ "status": "ERROR", "message": "...", "timestamp": "..." }`.

## Run locally

### Option A — Docker (no JDK needed)

```bash
docker compose up --build
```

Open http://localhost:8080.

### Option B — Maven + JDK 17

```bash
mvn clean spring-boot:run
```

or

```bash
mvn clean package
java -jar target/portfolio-backend-1.0.0.jar
```

Run tests with `mvn test`.

## Configuration

| Env var | Default | Purpose |
| --- | --- | --- |
| `PORT` | `8080` | HTTP port (Render injects this automatically) |
| `SPRING_PROFILES_ACTIVE` | — | Set to `prod` in deployment |
| `APP_CORS_ALLOWED_ORIGINS` | `*` | Comma-separated origins allowed to call `/api/**` |

The frontend uses a same-origin relative API path by default. If you ever host the HTML separately, set `window.PORTFOLIO_API_BASE = 'https://your-api-host'` before the inline script runs.

Inquiries are stored in an in-memory H2 database and also written to the application log. To use a managed database in production, set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` and add the matching JDBC driver to `pom.xml`.

## Deploy to Render

`render.yaml` is a Render Blueprint, so deployment is:

1. Push this repo to GitHub.
2. In Render, choose **New → Blueprint Instance** and select `Gautam677-creator/portfolio`.
3. Render reads `render.yaml`, builds the Dockerfile, and starts the service.
4. The public URL serves both the site and the API.

The `healthCheckPath` is `/actuator/health`. The blueprint uses the free plan in the `singapore` region; change `region` or `plan` in `render.yaml` if needed. Note that free services spin down when idle, so the first request after inactivity may take a few seconds — the frontend handles that with a timeout and a clear "demo mode" fallback.

## Repository layout

```
├── Dockerfile
├── docker-compose.yml
├── render.yaml
├── pom.xml
├── .github/workflows/ci.yml
└── src/main
    ├── java/com/gautamsingh/portfolio
    │   ├── GautamPortfolioApplication.java
    │   ├── config/       # CORS + validation error handling
    │   ├── controller/   # REST endpoints
    │   ├── model/        # DTOs + JPA entity
    │   ├── repository/   # Spring Data repository
    │   └── service/      # Contact persistence
    └── resources
        ├── application.properties
        └── static/index.html   # the portfolio frontend
```

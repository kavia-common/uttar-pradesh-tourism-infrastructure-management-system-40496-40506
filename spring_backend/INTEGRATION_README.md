# UPSTDC App Integration Notes

This document explains how to run the multi-container stack locally and in preview.

Components:
- Database: PostgreSQL (preview port 5001)
- Backend: Spring Boot (JWT secured REST API)
- Frontend: Angular dev server (port 3000)

Start order:
1) Database → 2) Backend → 3) Frontend

Database
- Preview DB is available on TCP 5001.
- Default DB name: myapp
- Default user: appuser
- Password: set via environment (SPRING_DATASOURCE_PASSWORD) or database initialization scripts as provided by ops.

Backend (Spring Boot)
- Port: configurable via SERVER_PORT (defaults to 8080).
- Datasource:
  - spring.datasource.url=jdbc:postgresql://localhost:5001/myapp
  - spring.datasource.username=appuser
  - spring.datasource.password=<from environment>
- JPA DDL:
  - spring.jpa.hibernate.ddl-auto is set to validate by default.
  - For local dev with evolving schema, set SPRING_JPA_HIBERNATE_DDL_AUTO=update.
- CORS:
  - Allowed origin defaults to http://localhost:3000
  - Authorization header is exposed and credentials are allowed.
- Security / JWT:
  - app.security.jwt.secret reads from APP_JWT_SECRET environment variable.
  - Supports Base64 or plain text secrets.
  - Token expiration can be set via APP_JWT_EXPIRATION_MS.

Required environment variables (example .env - do not commit secrets)
- SERVER_PORT=8080
- SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5001/myapp
- SPRING_DATASOURCE_USERNAME=appuser
- SPRING_DATASOURCE_PASSWORD=replace-with-secure-password
- SPRING_JPA_HIBERNATE_DDL_AUTO=validate
- APP_JWT_SECRET=replace-with-strong-secret # 256-bit+ recommended, Base64 allowed
- APP_JWT_EXPIRATION_MS=86400000
- CORS_ALLOWED_ORIGIN=http://localhost:3000
- APP_UPLOAD_DIR=uploads

Frontend (Angular)
- Development server runs on port 3000.
- Ensure API base URL is http://localhost:3001 (or as configured for backend reverse-proxy).
- Login flow uses the Authorization: Bearer <token> header; CORS exposes this header.

Reverse proxy and SSL (guidance)
- In production, terminate SSL at a reverse proxy (nginx/traefik) and forward to backend.
- Ensure proxy sets:
  - add_header Access-Control-Allow-Origin <frontend-origin>;
  - add_header Access-Control-Allow-Credentials true;
  - add_header Access-Control-Expose-Headers "Authorization, Content-Disposition";
- Typical upstream mapping:
  - https://your-domain/api -> http://backend:8080/api
- For Web servers, also proxy /swagger-ui.html and /v3/api-docs/* if documentation is exposed.

Health and docs
- Health: GET /actuator/health
- Swagger UI: /swagger-ui.html
- OpenAPI JSON: /v3/api-docs

Troubleshooting
- 401 Unauthorized:
  - Ensure APP_JWT_SECRET is set consistently and frontend sends Authorization header.
- CORS error:
  - Confirm CORS_ALLOWED_ORIGIN matches the exact frontend origin (scheme, host, and port).
- DB connection error:
  - Verify port 5001 and credentials. Override with SPRING_DATASOURCE_URL if needed.

# Event Registration System (Spring Boot + PostgreSQL + JWT)

A ready-to-run backend project for an Event Registration System.

## Features
- JWT authentication (register/login)
- Roles: USER, ADMIN
- Public events: list + details
- User registrations: register, list my registrations, cancel
- Admin panel (API): create/update/publish/cancel events, view registrations for an event
- Seat capacity + registration deadline enforced **transactionally** (pessimistic lock)
- PostgreSQL + Flyway migrations
- Swagger UI (springdoc)

## Requirements
- Java 17+
- Maven 3.9+

App runs on: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html

## Default admin (seeded on first run)
- Email: admin@example.com
- Password: Admin123!

## API overview
### Auth
- POST `/api/auth/register`
- POST `/api/auth/login`

### Public Events
- GET `/api/events`
- GET `/api/events/{id}`

### User (requires Bearer token)
- POST `/api/events/{id}/registrations`
- GET `/api/me/registrations`
- DELETE `/api/me/registrations/{registrationId}`

### Admin (requires ADMIN role)
- POST `/api/admin/events`
- PUT `/api/admin/events/{id}`
- PATCH `/api/admin/events/{id}/status`
- GET `/api/admin/events/{id}/registrations`
- DELETE `/api/admin/events/{id}`  (marks CANCELLED)


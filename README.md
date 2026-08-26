# Resource Booking System

A secure Spring Boot REST API for managing shared resources and reservations with JWT authentication, role-based access control, and MySQL/PostgreSQL support.

## Features

- JWT-based login at `POST /auth/login`
- Role-based access with `ADMIN` and `USER`
- Admin CRUD for resources and reservations
- User read-only access to resources and own reservations only
- Reservation status values: `PENDING`, `CONFIRMED`, `CANCELLED`
- Decimal price storage for bookings
- Filtering by status, min price, max price
- Pagination and sorting support
- Swagger/OpenAPI documentation
- Seed users for testing

## Tech Stack

- Java 17+
- Spring Boot 3.5+
- Spring Security
- Spring Data JPA / Hibernate
- JWT using JJWT
- MySQL or PostgreSQL
- Swagger UI

## Project Structure

- `spring_boot_backend_template/` - Maven project
- `src/main/java/com/sunbeam` - application code
- `src/test/java/com/sunbeam` - automated tests

## Prerequisites

- JDK 17 or newer
- Maven 3.9+
- MySQL 8+ or PostgreSQL 14+

## Environment Variables

Set the following variables before running the app:

```bash
export DB_URL=jdbc:mysql://localhost:3306/resource_booking?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
export DB_USERNAME=root
export DB_PASSWORD=root
export JWT_SECRET=myVeryStrongSecretKey1234567890!
export JWT_EXPIRATION=86400000
export PORT=8080
```

For PostgreSQL:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/resource_booking
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

## Database Configuration

The application uses MySQL by default. To switch to PostgreSQL, update the driver and dialect in the project configuration or use a PostgreSQL profile.

## Run the Application

```bash
cd spring_boot_backend_template
./mvnw spring-boot:run
```

On Windows:

```powershell
cd spring_boot_backend_template
mvnw.cmd spring-boot:run
```

## Seed Users

The application seeds these users automatically when the database is empty:

- Admin: `admin` / `admin123`
- User: `user` / `user123`

## API Examples

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Response:

```json
{
  "token": "<jwt>",
  "role": "ADMIN",
  "username": "admin"
}
```

### Get resources

```http
GET /resources
Authorization: Bearer <token>
```

### Create reservation

```http
POST /reservations
Authorization: Bearer <token>
Content-Type: application/json

{
  "resourceId": 1,
  "startTime": "2026-09-01T10:00:00",
  "endTime": "2026-09-01T12:00:00"
}
```

## Swagger

After starting the app, open:

```text
http://localhost:8080/swagger-ui.html
```

## Testing

Run the tests with the in-memory H2 profile:

```bash
cd spring_boot_backend_template
./mvnw test -Dspring.profiles.active=test -q
```

## Notes

- User identity is always resolved from the JWT.
- The request body may not override the authenticated user.
- Reservation operations for regular users are restricted to their own records.

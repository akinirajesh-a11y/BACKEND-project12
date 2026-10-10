# RentEase Backend - Spring Boot 3 & MySQL

This is the backend service for **RentEase – Online Property Rental Management System**.

## Tech Stack
- **Java 17+**
- **Spring Boot 3.3.4**
- **Spring Data JPA** & **Hibernate**
- **Spring Security** & **JWT (jjwt 0.12.6)**
- **MySQL 8.0** (with in-memory H2 fallback for tests)
- **Maven**

## Architecture
```
com.rentease/
├── config/        # Security, CORS, and DataSeeder
├── controller/    # REST API endpoints (Auth, Properties, Bookings, Payments, Agreements, Admin)
├── dto/           # Request and Response Transfer Objects
├── entity/        # JPA Entities (User, Property, PropertyImage, Booking, Payment, Agreement, Favorite)
├── exception/     # Custom exceptions and GlobalExceptionHandler
├── repository/    # Spring Data JPA Repositories
├── security/      # JWT Token Provider, UserDetailsService, AuthFilter, EntryPoint
└── service/       # Business logic and transactional service implementations
```

## Running Locally

### 1. Requirements
- JDK 17 or later installed
- Apache Maven 3.8+
- MySQL 8.0 running locally on port `3306`

### 2. Configure Local Database
Default configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rentease_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Build & Run
```bash
mvn clean package
mvn spring-boot:run
```
The server will start on port `8080`.

On first startup, `DataSeeder` automatically seeds demo accounts, property listings, and sample bookings.

## Demo Credentials
| Role | Email | Password |
|---|---|---|
| **Admin** | `admin@rentease.com` | `admin123` |
| **Property Owner** | `owner@rentease.com` | `owner123` |
| **Tenant** | `tenant@rentease.com` | `tenant123` |

## Running Tests
```bash
mvn clean test
```
Runs unit and integration tests using in-memory H2 database.

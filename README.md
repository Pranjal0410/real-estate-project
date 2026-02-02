# Real Estate Investment Platform

A production-grade Real Estate Investment Platform built with Spring Boot, featuring JWT authentication with refresh token rotation, ACID-compliant investment transactions, and comprehensive test coverage.

## Key Features

- **JWT Authentication with Refresh Token Rotation** - Secure token-based auth with automatic rotation and token family tracking
- **Investment Portfolio System** - Create portfolios, buy/sell property shares, track holdings and gains
- **ACID-Compliant Transactions** - Optimistic and pessimistic locking for financial integrity
- **Role-Based Access Control** - ADMIN, INVESTOR, ANALYST, PROPERTY_OWNER, AGENT roles
- **95%+ Test Coverage** - Comprehensive unit, integration, and controller tests with JaCoCo

## Technology Stack

| Category | Technologies |
|----------|-------------|
| **Backend** | Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA, Hibernate |
| **Database** | MySQL 8+, H2 (testing) |
| **Security** | JWT (access + refresh tokens), BCrypt, Rate Limiting |
| **Testing** | JUnit 5, Mockito, Spring Test, JaCoCo |
| **Build** | Maven, JaCoCo for coverage |
| **Frontend** | JSP, Bootstrap 5, jQuery |

## Quick Start

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.6+

### Setup & Run

```bash
# 1. Clone the repository
git clone <repository-url>
cd real-estate-project

# 2. Create MySQL database
mysql -u root -p -e "CREATE DATABASE realestate_db;"

# 3. Update credentials in application.yml (if needed)
# Default: root/root

# 4. Build and run
./mvnw clean install -DskipTests
./mvnw spring-boot:run

# 5. Access the application
# Web UI: http://localhost:9090
# Swagger: http://localhost:9090/swagger-ui.html
```

### Run Tests
```bash
# Run all tests
./mvnw test

# Run with coverage report
./mvnw verify

# View coverage report
open target/site/jacoco/index.html
```

## API Reference

### Authentication Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login, get access + refresh tokens | Public |
| POST | `/api/auth/refresh` | Rotate refresh token (rate limited) | Public |
| POST | `/api/auth/logout` | Blacklist token, logout | Bearer |
| GET | `/api/auth/sessions` | List active sessions | Bearer |
| DELETE | `/api/auth/sessions/{id}` | Revoke specific session | Bearer |
| GET | `/api/auth/current` | Get current user info | Bearer |

### Portfolio Endpoints

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| POST | `/api/portfolios` | Create portfolio | INVESTOR, ADMIN |
| GET | `/api/portfolios` | Get my portfolios | INVESTOR, ADMIN, ANALYST |
| GET | `/api/portfolios/{id}` | Get portfolio by ID | INVESTOR, ADMIN, ANALYST |
| GET | `/api/portfolios/{id}/holdings` | Get portfolio holdings | INVESTOR, ADMIN, ANALYST |
| GET | `/api/portfolios/{id}/summary` | Get portfolio summary | INVESTOR, ADMIN, ANALYST |
| PUT | `/api/portfolios/{id}` | Update portfolio | INVESTOR, ADMIN |
| DELETE | `/api/portfolios/{id}` | Close portfolio | INVESTOR, ADMIN |
| GET | `/api/portfolios/admin/all` | Get all portfolios | ADMIN |

### Transaction Endpoints

| Method | Endpoint | Description | Roles |
|--------|----------|-------------|-------|
| POST | `/api/transactions/buy` | Buy property shares | INVESTOR, ADMIN |
| POST | `/api/transactions/sell` | Sell property shares | INVESTOR, ADMIN |
| POST | `/api/transactions/transfer` | Transfer between portfolios | INVESTOR, ADMIN |
| GET | `/api/transactions/portfolio/{id}` | Get transaction history | INVESTOR, ADMIN, ANALYST |
| GET | `/api/transactions/{reference}` | Get transaction by reference | INVESTOR, ADMIN, ANALYST |
| POST | `/api/transactions/admin/{id}/reverse` | Reverse transaction | ADMIN |

### Property Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/properties` | List all properties | Public |
| GET | `/api/properties/{id}` | Get property by ID | Public |
| POST | `/api/properties` | Create property | ADMIN, OWNER |
| PUT | `/api/properties/{id}` | Update property | ADMIN, OWNER |
| DELETE | `/api/properties/{id}` | Delete property | ADMIN |

## Usage Examples

### Register & Login
```bash
# Register
curl -X POST http://localhost:9090/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "investor1",
    "email": "investor@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'

# Login
curl -X POST http://localhost:9090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "investor1", "password": "password123"}'

# Response includes accessToken and refreshToken
```

### Create Portfolio & Buy Property
```bash
# Create portfolio
curl -X POST http://localhost:9090/api/portfolios \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"portfolioName": "My Portfolio", "riskProfile": "MODERATE"}'

# Buy property shares
curl -X POST http://localhost:9090/api/transactions/buy \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "portfolioId": 1,
    "propertyId": 1,
    "quantity": "1.0",
    "idempotencyKey": "unique-key-123"
  }'
```

### Refresh Token
```bash
curl -X POST http://localhost:9090/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken": "YOUR_REFRESH_TOKEN"}'
```

## Project Structure

```
src/
├── main/java/com/realestate/
│   ├── config/           # Security, Retry, Scheduling configs
│   ├── controller/       # REST controllers
│   │   ├── AuthController.java
│   │   ├── PortfolioController.java
│   │   └── TransactionController.java
│   ├── model/
│   │   ├── entity/       # JPA entities
│   │   │   ├── User.java
│   │   │   ├── Portfolio.java
│   │   │   ├── Holding.java
│   │   │   ├── InvestmentTransaction.java
│   │   │   ├── RefreshToken.java
│   │   │   └── TokenBlacklist.java
│   │   └── dto/          # Request/Response DTOs
│   ├── repository/       # Spring Data repositories
│   ├── security/         # JWT provider, filters
│   ├── service/          # Business logic
│   │   ├── TokenService.java
│   │   ├── RateLimitService.java
│   │   ├── PortfolioService.java
│   │   └── InvestmentTransactionService.java
│   └── exception/        # Custom exceptions
├── main/resources/
│   ├── application.yml
│   └── schema.sql
└── test/java/com/realestate/
    ├── testutil/         # Test builders & factories
    ├── unit/service/     # Service unit tests
    ├── unit/security/    # Security tests
    ├── controller/       # Controller tests
    ├── repository/       # Repository tests
    └── integration/      # Integration tests
```

## Security Features

### JWT Token System
- **Access Token**: 15-minute expiry, contains JTI for blacklisting
- **Refresh Token**: 7-day expiry, family-based tracking
- **Token Rotation**: Old refresh tokens marked as USED, new pair issued
- **Reuse Detection**: If USED token is reused, entire family is invalidated (COMPROMISED)
- **Blacklisting**: Logout blacklists access token JTI

### Rate Limiting
- Refresh endpoint: 10 attempts per 15 minutes
- Exceeded limit: 30-minute block
- Caffeine cache-based implementation

### Transaction Locking
| Transaction | Lock Type | Isolation Level |
|-------------|-----------|-----------------|
| BUY | Optimistic (Portfolio), Pessimistic (Property) | REPEATABLE_READ |
| SELL | Pessimistic (Holding) | SERIALIZABLE |
| TRANSFER | Pessimistic (Both Holdings) | SERIALIZABLE |

## User Roles

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access, reverse transactions, view all portfolios |
| **INVESTOR** | Create portfolios, buy/sell/transfer, view own data |
| **ANALYST** | Read-only access to portfolios and transactions |
| **PROPERTY_OWNER** | Manage own properties |
| **AGENT** | Property listings |

## Default Credentials

| Username | Password | Role |
|----------|----------|------|
| admin | password123 | ADMIN |
| john_investor | password123 | INVESTOR |
| jane_owner | password123 | PROPERTY_OWNER |

## Configuration

Key settings in `application.yml`:

```yaml
spring:
  security:
    jwt:
      access-expiration: 900000      # 15 minutes
      refresh-expiration: 604800000  # 7 days
  datasource:
    hikari:
      maximum-pool-size: 20
```

## Test Coverage

The project includes comprehensive tests:

- **Unit Tests**: TokenService, RateLimitService, PortfolioService, InvestmentTransactionService
- **Security Tests**: JwtTokenProvider
- **Controller Tests**: AuthController, PortfolioController, TransactionController
- **Repository Tests**: PortfolioRepository, RefreshTokenRepository
- **Integration Tests**: AuthFlow, InvestmentFlow

Run `./mvnw verify` and check `target/site/jacoco/index.html` for coverage report.

## License

This project is developed for educational purposes as part of the Advanced Programming Concepts course.

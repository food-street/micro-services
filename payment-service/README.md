# Payment Service

This service handles payment processing for food orders in the Food Street application.

## Prerequisites

- Java 21
- Maven
- Docker and Docker Compose
- MySQL 8.3.0

## Setup

1. Start MySQL container:
```bash
docker compose up -d
```

2. Build the application:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The service will be available at http://localhost:8081

## API Endpoints

- `POST /api/v1/payments/initiate` - Initiate a new payment
- `POST /api/v1/payments/{paymentId}/capture` - Capture a payment
- `GET /api/v1/payments/{paymentId}/status` - Get payment status
- `POST /api/v1/payments/{paymentId}/refund` - Process refund

## Database

The service uses MySQL with the following databases:
- `payment_db` - Main database for payment records

## Development

The project uses:
- Spring Boot 3.4.4
- Flyway for database migrations
- JPA for database access
- Lombok for reducing boilerplate code 
# Load & Booking Management System

A robust backend system built with Spring Boot and PostgreSQL to efficiently manage loads and bookings in logistics operations. The system provides REST APIs for handling loads and bookings with CRUD operations, optimized for performance, security, and scalability.

## Features

- **Load Management**: Create, retrieve, update, and delete load entries
- **Booking Management**: Create, retrieve, update, and delete booking entries
- **Status Tracking**: Automatic status transitions based on business rules
- **Input Validation**: Comprehensive validation for all API inputs
- **Error Handling**: Structured error responses with meaningful messages
- **Logging**: Detailed logging for operations and exceptions

## Tech Stack

- **Framework**: Spring Boot 3.4.4
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Java Version**: 17

## Setup Instructions

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Database Setup

1. Create a PostgreSQL database:

```sql
CREATE DATABASE logistics_db;
```

2. Ensure you have a PostgreSQL user with appropriate permissions, or use the default `postgres` user.

### Application Configuration

1. Clone the repository:

```bash
git clone https://github.com/yourusername/load-booking-service.git
cd load-booking-service
```

2. Configure database connection in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/logistics_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA Properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=ERROR
logging.level.com.logistics=DEBUG
```

### Build and Run (Optional, if using IDE)

1. Build the application:

```bash
mvn clean install
```

2. Run the application:

```bash
mvn spring-boot:run
```

The server will start on port 8080 by default.

## API Documentation

### Load Management

#### Create a New Load

- **Endpoint**: `POST /load`
- **Request Body**:

```json
{
  "shipperId": "SHP12345",
  "facility": {
    "loadingPoint": "Mumbai",
    "unloadingPoint": "Delhi",
    "loadingDate": "2025-04-20T10:00:00",
    "unloadingDate": "2025-04-22T14:00:00"
  },
  "productType": "Electronics",
  "truckType": "Flatbed",
  "noOfTrucks": 2,
  "weight": 1500.0,
  "comment": "Handle with care"
}
```

- **Response**: Created load object with HTTP 201 status

#### Get All Loads

- **Endpoint**: `GET /load`
- **Optional Query Parameters**:
  - `shipperId`: Filter by shipper ID
  - `truckType`: Filter by truck type
- **Response**: Array of load objects

#### Get Load by ID

- **Endpoint**: `GET /load/{loadId}`
- **Response**: Load object

#### Update Load

- **Endpoint**: `PUT /load/{loadId}`
- **Request Body**: Same as create load
- **Response**: Updated load object

#### Delete Load

- **Endpoint**: `DELETE /load/{loadId}`
- **Response**: HTTP 204 No Content

### Booking Management

#### Create a New Booking

- **Endpoint**: `POST /booking`
- **Request Body**:

```json
{
  "loadId": "UUID-from-load",
  "transporterId": "TRP54321",
  "proposedRate": 15000.0,
  "comment": "Booking request"
}
```

- **Response**: Created booking object with HTTP 201 status

#### Get All Bookings

- **Endpoint**: `GET /booking`
- **Optional Query Parameters**:
  - `transporterId`: Filter by transporter ID
  - `loadId`: Filter by load ID
- **Response**: Array of booking objects

#### Get Booking by ID

- **Endpoint**: `GET /booking/{bookingId}`
- **Response**: Booking object

#### Update Booking

- **Endpoint**: `PUT /booking/{bookingId}`
- **Request Body**:

```json
{
  "proposedRate": 16000.0,
  "comment": "Updated booking request",
  "status": "ACCEPTED"
}
```

- **Response**: Updated booking object

#### Delete Booking

- **Endpoint**: `DELETE /booking/{bookingId}`
- **Response**: HTTP 204 No Content

## Business Rules

1. **Load Status Transitions**:
   - Default status for new loads: `POSTED`
   - When a booking is created: `BOOKED`
   - When a booking is deleted: `CANCELLED`

2. **Booking Rules**:
   - Bookings cannot be created for loads with `CANCELLED` status
   - When a booking is accepted, its status is updated to `ACCEPTED`
   - Multiple bookings can be created for a single load

## Error Handling

The API returns structured error responses for various scenarios:

- **400 Bad Request**: Invalid input data with validation errors
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Unexpected server issues

Example error response:

```json
{
  "status": 400,
  "message": "Unloading date must be after loading date",
  "timestamp": "2025-04-17T09:51:48.252015"
}
```

## Assumptions

1. **Database Availability**: PostgreSQL server will be correctly set up and accessible during the development and deployment phases.

2. **Unique Identifiers**: UUIDs for both Load and Booking entities are generated and managed by the backend (or database) to ensure uniqueness.

3. **Consistent Status Flow**: The status of both Load and Booking will only transition through valid states as defined in the requirements (POSTED → BOOKED → CANCELLED for Load, PENDING → ACCEPTED/REJECTED for Booking).

4. **Atomic Operations**: Whenever a Booking is created, the corresponding Load status update (to BOOKED) happens atomically to avoid data inconsistency.

5. **External User Inputs**: It is assumed that the shipperId and transporterId provided during API requests are valid and refer to existing entities in an external user management system.

6. **Date Validations**: loadingDate and unloadingDate are assumed to be valid timestamps, and unloadingDate must be later than loadingDate. Basic validation will be implemented, but logical correctness (real-world dates) is assumed at input time.

7. **Deployment Environment**: The service will run in an environment that supports Java 17+, Spring Boot 3.x, and PostgreSQL 14+.

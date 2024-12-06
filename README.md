# Courier Tracking Application

This application tracks couriers and their visits to stores, calculating total travel distances and monitoring store entries.

## Table of Contents
1. [Overview](#overview)
2. [Design Patterns Used](#design-patterns-used)
   - [Observer Pattern](#1-observer-pattern)
   - [Strategy Pattern](#2-strategy-pattern)
   - [Factory Pattern](#3-factory-pattern)
3. [Technologies Used](#technologies-used)
4. [Running the Application](#running-the-application)
   - [Using Docker Compose](#using-docker-compose)
5. [API Documentation](#api-documentation)
   - [API Endpoints](#api-endpoints)
   - [Example Requests with cURL](#example-requests-with-curl)
6. [Testing](#testing)

## Design Patterns Used

### 1. Observer Pattern
- Used in location tracking to notify when a courier enters store proximity

### 2. Strategy Pattern
- Applied in distance calculation algorithms
- Allows switching between different distance calculation methods (Haversine, Euclidean)

### 3. Factory Pattern
- Implemented in `DistanceCalculationFactory` for creating distance calculation strategies
- Provides flexibility in choosing calculation methods based on requirements

## Technologies Used

- Java 21
- Spring Boot 3.3.5
- PostgreSQL with PostGIS
- Docker & Docker Compose
- Gradle
- TestContainers
- Swagger/OpenAPI

## Running the Application

### Using Docker Compose

1. Clone the repository and navigate into the project directory:
   ```bash
   git clone https://github.com/yourusername/courier-tracking.git
   cd courier-tracking
   ```

2. Build and run the application using Docker Compose:
   ```bash
   docker-compose up
   ```

   The application will be available at `http://localhost:8080`

## API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui/index.html`

### API Endpoints

#### Courier Endpoints
1. **Create Courier**
   - `POST /api/v1/courier`
   - Description: Creates a new courier

2. **Track Courier Location**
   - `POST /api/v1/courier/{courierId}/locations`
   - Description: Tracks courier's current location

3. **Get Courier's Total Distance**
   - `GET /api/v1/courier/{courierId}/total-distance`
   - Description: Returns total travel distance of a courier

4. **Get Courier by ID**
   - `GET /api/v1/courier/{courierId}`
   - Description: Returns courier details by ID

5. **Delete Courier**
   - `DELETE /api/v1/courier/{courierId}`
   - Description: Deletes a courier by ID

6. **Get Store Visit History**
   - `GET /api/v1/courier/{courierId}/history`
   - Description: Returns paginated store visit history for a courier

#### Store Endpoints
1. **Create Store**
   - `POST /api/v1/store`
   - Description: Creates a new store

2. **Get Store by ID**
   - `GET /api/v1/store/{storeId}`
   - Description: Returns store details by ID

3. **Delete Store**
   - `DELETE /api/v1/store/{storeId}`
   - Description: Deletes a store by ID

### Example Requests with cURL

#### Create a Courier
```bash
curl -X POST 'http://localhost:8080/api/v1/courier' \
-H 'Content-Type: application/json' \
-d '{
    "name": "Necati Hasim",
    "identityNumber": "12345678901"
}'
```

#### Track Courier Location
```bash
curl -X POST 'http://localhost:8080/api/v1/courier/1/locations' \
-H 'Content-Type: application/json' \
-d '{
    "lat": 40.9923307,
    "lng": 29.1244229,
    "time": "2024-01-20T10:30:00"
}'
```

#### Get Courier's Total Distance
```bash
curl -X GET 'http://localhost:8080/api/v1/courier/1/total-distance'
```

#### Get Store Visit History
```bash
curl -X GET 'http://localhost:8080/api/v1/courier/1/history?page=0&pageSize=10'
```

#### Create a Store
```bash
curl -X POST 'http://localhost:8080/api/v1/store' \
-H 'Content-Type: application/json' \
-d '{
    "name": "Test Store",
    "latitude": 41.0082,
    "longitude": 28.9784
}'
```

#### Get Store by ID
```bash
curl -X GET 'http://localhost:8080/api/v1/store/1'
```

#### Delete Store
```bash
curl -X DELETE 'http://localhost:8080/api/v1/store/1'
```

---
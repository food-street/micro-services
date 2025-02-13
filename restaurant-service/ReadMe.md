## Restaurant Service

Cursor created a complete restaurant service with the following features:

1. Restaurant entity with fields:
   - id
   - name
   - description
   - imageUrl
   - courtId (to link with food courts)
   - createdAt
   - updatedAt

2. REST endpoints:
   - POST /restaurant - Create a new restaurant
   - GET /restaurant - Get all restaurants
   - GET /restaurant/{id} - Get a specific restaurant
   - GET /restaurant/court/{courtId} - Get all restaurants in a food court
   - PUT /restaurant/{id} - Update a restaurant
   - DELETE /restaurant/{id} - Delete a restaurant

3. Database:
   - MySQL database with Flyway migrations
   - Docker Compose setup with MySQL container
   - Port 3307 for MySQL to avoid conflict with court service

4. Features:
   - Validation for all fields
   - Error handling with custom error codes
   - Court existence verification before creating/updating restaurants
   - Timestamps for creation and updates
   - Integration with the court service

5. Configuration:
   - Application properties for different environments
   - Port 8084 for the service
   - Logging configuration

The service is designed to work alongside the court service, with proper validation and error handling. The restaurant service verifies the existence of a court before allowing operations that reference a court ID.

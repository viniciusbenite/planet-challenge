# Planet

This application allows users to make book reservations.

## Getting Started

To run the application you need Maven or Docker installed.

1. Clone the repository:
   ```bash
   git clone https://github.com/viniciusbenite/planet-challenge.git
2. Go to the application folder and use docker compose to start the application:
   ```bash
   docker-compose up --build
   ```
   This will spin up all needed containers: Backend services, Postgres, Redis and RabbitMQ.   
4. Alternatively, compile and run the backend repository using Maven.
5. Reservation service can be access through http://localhost:8082/api/v1

## API docs
API documentation can be found at http://localhost:8082/api/v1/swagger-ui/index.html#/

## Operations implemented
- Reserve a book.
- Retrieve an existing reservation by ID.
- Retrieve all reservations for a specific user a.k.a reservation history.
- Cancel a reservation.
- Pickup reservation.
- Expire reservations after 7 days.

## Business Logic
- Prevent a user from exceeding the max reservation limit.
- Update the book’s available copies when a reservation is made, expired or canceled.
- No duplicated reservations.

## Extras
Added a service to mock notification sends to users when creating a reservation and for failed reservations.

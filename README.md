# Order and Notification System with Kafka

I built this project to learn how separate Spring Boot applications
communicate through Kafka. During my internship, I worked on a monolithic
application, so I wanted to understand what changes when a feature is
split across services.

The project has two services. Order Service accepts and stores orders,
then publishes an event to Kafka. Notification Service reads that event
and saves a confirmation message in its own database.

There is no real email integration—the notification is a stored message
that can be retrieved through an API.

## Tech Stack

- Java 17 and Spring Boot 4.1.2
- Spring Data JPA and PostgreSQL
- Apache Kafka and Spring Kafka
- Docker and Docker Compose
- Render, Aiven Kafka, and Neon PostgreSQL

## Architecture

```text
Client → Order Service → Order Database
              │
              ▼
         Aiven Kafka
              │
              ▼
     Notification Service → Notification Database
```
Each service owns its database. Notification Service receives order
information through Kafka rather than querying the Order database.

## Features

- Create and retrieve orders
- Validate order requests with field-level error messages
- Publish order-created events with the order ID as the message key
- Consume events and persist simulated notifications
- Skip previously processed events using their event IDs
- Retry processing failures twice before forwarding to a dead-letter topic
- Retrieve notifications by order ID

## API Endpoints

| Service | Method | Endpoint |
|---|---|---|
| Order | POST | `/api/orders` |
| Order | GET | `/api/orders/{id}` |
| Notification | GET | `/api/notifications?orderId={id}` |

## Verified Scenarios

- An order produces a notification through Kafka.
- Pending events are processed after Notification Service restarts.
- Replaying a processed event does not create another notification.
- A simulated processing failure is retried and forwarded to the DLT.
- Normal processing continues after a failed event is recovered.

## Try it

The services are deployed on Render:

- Order Service: https://sawan-order-service.onrender.com
- Notification Service: https://sawan-notification-service.onrender.com

These use free hosting, so the first request may take a minute to
respond while a service wakes up. Open both service URLs before testing.
The root URLs may return 404 because the APIs use the paths below.

### 1. Create an order

In Postman, send:

POST https://sawan-order-service.onrender.com/api/orders

Select Body → raw → JSON and use:

```json
{
  "productName": "Keyboard",
  "quantity": 2,
  "unitPrice": 1499.00
}
```

A successful request returns `201 Created` with the saved order.
Copy the `id` from the response.

### 2. Retrieve the order

GET https://sawan-order-service.onrender.com/api/orders/order_id

Replace `ORDER_ID` with the ID you copied.

### 3. Check the notification

GET https://sawan-notification-service.onrender.com/api/notifications?orderId=...

The response should contain a confirmation message for the same order.

Notifications are processed asynchronously. If the response is an empty
array (`[]`), wait a few seconds and try again.

## Run locally

### Requirements

- Java 17 or newer
- Docker Desktop with Docker Compose
- PostgreSQL connection details for two databases:
  `order_db` and `notification_db`

The project includes Maven wrappers, so a separate Maven installation
isn't required.

### 1. Clone the repository

```bash
git clone https://github.com/sawansumankumar/kafka-order-notification-system.git
cd kafka-order-notification-system
```

### 2. Start Kafka

With Docker Desktop running:

```bash
docker compose up -d
```

Create the topics after Kafka is ready:

```bash
docker compose exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic order-created --partitions 1 --replication-factor 1
```

```bash
docker compose exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic order-created.DLT --partitions 1 --replication-factor 1
```

### 3. Configure database connections

In your IDE's Order Service run configuration, set:

```text
ORDER_DB_URL=jdbc:postgresql://YOUR_HOST/order_db
ORDER_DB_USERNAME=YOUR_USERNAME
ORDER_DB_PASSWORD=YOUR_PASSWORD
```

In Notification Service's run configuration, set:

```text
NOTIFICATION_DB_URL=jdbc:postgresql://YOUR_HOST/notification_db
NOTIFICATION_DB_USERNAME=YOUR_USERNAME
NOTIFICATION_DB_PASSWORD=YOUR_PASSWORD
```

For hosted PostgreSQL, include the TLS parameters required by your
provider in the JDBC URLs.

Leave `SPRING_PROFILES_ACTIVE` unset for this local Kafka setup.
If `KAFKA_BOOTSTRAP_SERVERS` is already configured, remove it or set
it to `localhost:9092`.

Database tables are created from the JPA entities at application startup.

### 4. Run both services

Open each service as a Maven project and run:

- `OrderServiceApplication`
- `NotificationServiceApplication`

Their local addresses are:

- Order Service: http://localhost:8081
- Notification Service: http://localhost:8082

Use the API paths in the “Try it” section with these local addresses.

### 5. Stop Kafka

```bash
docker compose down
```

This preserves Kafka's named data volume. Adding `-v` also deletes
the volume and its stored events.


## Limitations

- Notifications are stored confirmations; no actual email is sent.
- Saving an order and publishing its event are separate operations.
  A transactional outbox would address this consistency gap.
- Free Render services can sleep, delaying notification processing.
- Aiven's free Kafka service can shut down when inactive and lose topic data.
- The API has no authentication and is intended for demonstration.

## What I learned

One useful test was stopping Notification Service while continuing to
create orders. When I restarted it, it processed the pending Kafka
events and created the notifications.

I also learned why consumers need duplicate handling. If the same event
arrives again, the service checks its event ID and skips creating another
notification.

The database save and Kafka publish are still separate operations.
This is a limitation of the current implementation; a transactional
outbox would be a possible improvement.

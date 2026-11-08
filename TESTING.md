
# Testing - TrackFlow Order Service

## Running tests

Windows:

```bash
.\mvnw.cmd clean verify
```

Linux/macOS:

```bash
./mvnw clean verify
```

## Persistence Tests

### OrderPersistenceTest

The persistence tests validate:

- Customer registration and document lookup.
- Order persistence with associated customer.
- Origin and destination address relationships.
- Initial order status.
- Automatic creation of the initial status history.
- Pagination of orders by authenticated user ID.

## Database requirements

- PostgreSQL running.
- Database `trackflow_orders` created.
- Environment variables configured.

## Database migrations

Flyway automatically executes migrations
located at:

`src/main/resources/db/migration`

Hibernate uses `ddl-auto=validate` to verify
the entity mappings against the database schema.

## Test isolation

Tests use the configured PostgreSQL database
and run within Spring's transactional test context.

Changes are rolled back after each test.

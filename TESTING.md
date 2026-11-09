
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



## Testes unitários — Camada de serviços

Os testes da camada de serviços utilizam JUnit 5 e Mockito.

| Classe | Cenários testados |
|---|---|
| CustomerServiceTest | Cadastro de cliente e rejeição de documento duplicado |
| AddressServiceTest | Cadastro de endereço |
| TrackingCodeGeneratorTest | Formato e geração dos códigos |
| OrderServiceTest | Criação de pedido, cliente inexistente, colisão de código e transições de status |

### Executar os testes unitários da Etapa 3

```bash
./mvnw -Dtest=CustomerServiceTest,AddressServiceTest,TrackingCodeGeneratorTest,OrderServiceTest test
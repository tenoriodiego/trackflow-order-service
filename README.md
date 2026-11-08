# trackflow-order-service
# TrackFlow - Order Service

Microsserviço responsável pelo gerenciamento
de pedidos da plataforma TrackFlow.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Maven
- JUnit 5
- Mockito

## Responsabilidades

- Cadastro de pedidos.
- Consulta de pedidos.
- Atualização de pedidos.
- Cancelamento de pedidos.
- Gerenciamento de status.
- Histórico de alterações dos pedidos.

## Arquitetura

O Order Service é responsável exclusivamente
pelas operações relacionadas aos pedidos.

A autenticação e o gerenciamento de usuários
são responsabilidades do Auth Service.

## Configuração local

1. Clone o repositório.
2. Configure o Java 21.
3. Configure o PostgreSQL.
4. Crie o banco `trackflow_orders`.
5. Copie `.env.example` para `.env`.
6. Configure suas credenciais no `.env`.

## Executando a aplicação

Windows:

```bash
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Porta padrão: `8081`.

## Testes

Windows:

```bash
.\mvnw.cmd clean verify
```


## Domain Model

The Order Service manages the logistics operations
and shipment tracking process.

### Entities

- **Customer:** Customer responsible for the shipment.
- **Address:** Origin and destination addresses.
- **Order:** Main shipment information and tracking code.
- **OrderStatusHistory:** Historical record of shipment status changes.

### Order Status Lifecycle

- CREATED
- COLLECTED
- IN_TRANSIT
- OUT_FOR_DELIVERY
- DELIVERED
- CANCELLED

### Persistence

- PostgreSQL for relational data storage.
- Spring Data JPA for persistence operations.
- Flyway for database versioning and migrations.
- UUID as the primary key strategy.

### Database Tables

- customers
- addresses
- orders
- order_status_history

## Development Progress

- [x] Initial project structure.
- [x] Maven and PostgreSQL configuration.
- [x] Flyway configuration.
- [x] Domain entities.
- [x] Order status enumeration.
- [x] JPA repositories.
- [x] Initial database migration.
- [x] Persistence tests.
- [ ] Business rules and services.
- [ ] REST API endpoints.
- [ ] JWT integration.
- [ ] Integration tests.
- [ ] Deployment.
- [ ] RabbitMQ integration.

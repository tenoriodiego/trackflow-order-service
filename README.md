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

## Status do desenvolvimento

- [x] Estrutura inicial.
- [x] Configuração do Maven.
- [x] Configuração do PostgreSQL.
- [x] Configuração do Flyway.
- [ ] Modelagem das entidades.
- [ ] Implementação das regras de negócio.
- [ ] Integração com Auth Service.
- [ ] Testes de integração.
- [ ] Deploy.
- [ ] Integração com RabbitMQ.

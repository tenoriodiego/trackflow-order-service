
# Testing - TrackFlow Order Service

## Executando os testes

Para executar todos os testes:

```bash
./mvnw clean verify
```

No Windows:

```bash
.\mvnw.cmd clean verify
```

## Testes implementados

### TrackFlowOrderServiceApplicationTests

Verifica:

- Inicialização do contexto Spring.
- Carregamento das configurações.
- Integração com o banco PostgreSQL.
- Inicialização do Flyway.

## Pré-requisitos

- Java 21.
- PostgreSQL em execução.
- Banco `trackflow_orders` criado.
- Variáveis de ambiente configuradas.

## Estratégia de testes

O projeto utilizará:

- JUnit 5 para testes automatizados.
- Mockito para isolamento de dependências.
- Spring Boot Test para testes de integração.
- Testes de repositórios e APIs nas próximas etapas.

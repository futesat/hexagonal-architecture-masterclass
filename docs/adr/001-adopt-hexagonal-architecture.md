# 1. Adopt Hexagonal Architecture

Date: 2025-12-14

> 🇪🇸 [Versión en Español](001-adoptar-arquitectura-hexagonal.md)

## Status
Accepted

## Context
The project requires rich business logic that is independent of frameworks, databases, and external user interfaces. We want to ensure that the domain core can evolve without being affected by infrastructure changes (e.g., switching from H2 to PostgreSQL, or from REST to GraphQL).

## Decision
We will adopt **Hexagonal Architecture (Ports & Adapters)**.

## Consequences

### Positive
*   **Decoupling**: The domain is pure Java and is unaware of Spring Boot or the database.
*   **Testability**: We can test all business logic with fast unit tests, without loading Spring contexts.
*   **Flexibility**: We can change the persistence adapter (Infrastructure) without touching a single line of the domain.

### Negative
*   **Initial Complexity**: Requires creating more classes (Interfaces/Ports, Adapters, DTOs) than a traditional 3-layer MVC architecture.
*   **Data Mapping**: Need to duplicate data models (Domain Entity vs JPA Entity) and create converters.

## Compliance
*   Domain classes must not have `import org.springframework...`.
*   All communication with the outside world must be done through interfaces (Ports).
*   Architecture tests (ArchUnit) will be used to automatically guarantee these rules.

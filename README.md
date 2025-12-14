# Didactic Example: Hexagonal Architecture with Spring Boot

[![CI](https://github.com/futesat/hexagonal-architecture-masterclass/actions/workflows/maven.yml/badge.svg)](https://github.com/futesat/hexagonal-architecture-masterclass/actions/workflows/maven.yml)
[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

> 🇪🇸 [Versión en Español](README_es.md)

This project is a practical and didactic example designed to explain how to integrate advanced software engineering concepts into a Java application with Spring Boot.

> **🤖 For AI Agents**: If you are an AI agent working with this project, consult [`AGENTS.md`](AGENTS.md) for specific instructions, commands, architecture rules, and best practices.

> **✨ Powered by AI**: This project has been created and refactored with the help of the **Gemini 3** assistant.

## 🚀 Concepts Implemented

### 1. DDD (Domain-Driven Design)
The core of the software is the **Domain** (business logic), and it must be isolated from technical details.
- **Rich Entities**: `Course` is not just 'data', it contains logic and validations.
- **Value Objects**: `CourseId` and `CourseName`. We avoid using primitive types (`String`, `int`) for domain concepts. This prevents errors (e.g., passing a name where an ID is expected) and encapsulates validation rules.

### 2. Hexagonal Architecture (Ports & Adapters)
Divides the application into **Inside** (Domain + Application) and **Outside** (Infrastructure).
- **Ports**: Interfaces defined in the domain (`CourseRepository`). The domain states *what* it needs, but not *how* it is done.
- **Adapters**: Implementations in the infrastructure layer.
    - **Driver (Input)**: `CoursePostController` (REST API).
    - **Driven (Output)**: `InMemoryCourseRepository` (Database).

### 3. Clean Code
Readable and maintainable code.
- **Expressive Names**: Classes and methods say exactly what they do.
- **Short Methods**: Single responsibility (SOLID).
- **Semantic Constructors**: Use of static factory methods (`Course.create(...)`) instead of complex public constructors.

### 4. CQRS (Command Query Responsibility Segregation)
Separation of Write (Command) and Read (Query) operations.
- **Write Side (Command)**: Optimized for consistency and business rules.
    - `CreateCourseCommand`: User intention.
    - `CreateCourseCommandHandler`: Logic that modifies state.
- **Read Side (Query)**: Optimized for the client view.
    - `FindCourseQueryHandler`: Fetches data and returns plain DTOs (`CourseResponse`). We never expose the Domain Entity directly in reading to avoid coupling the API to internal rules.

### 5. Shared Kernel
Reusable code between different Bounded Contexts (modules).
- **Identifier**: Abstract base class (`shared.domain.Identifier`) that encapsulates UUID logic and validation. `CourseId` inherits from it, avoiding code duplication if we create `StudentId` tomorrow.

### 6. Global Error Handling
Transformation of domain exceptions into coherent HTTP responses.
- **GlobalExceptionHandler**: Uses Spring's `@ControllerAdvice` to capture exceptions:
    - `IllegalArgumentException` (Validation) -> **400 Bad Request**.
    - `CourseNotFound` (Not exists) -> **404 Not Found**.

### 7. TDD (Test-Driven Development)
Development guided by tests at all levels.
- **Domain Unit**: `CourseNameTest` and `CourseIdTest` shield business rules (validations, nulls...).
- **Application Unit**: `CreateCourseCommandHandlerTest` verifies orchestration (mocks).
- **API Integration**: `CoursePostControllerTest` verifies the HTTP web layer simulating real requests.

### 8. Domain Events
Mechanism to decouple side effects (send email, logs, analytics).
- **Core**: `AggregateRoot` (Shared Kernel) allows entities to record what happened (`record()`).
- **Event**: `CourseCreatedEvent` captures that a course was created.
- **Publication**: The Handler retrieves events from the aggregate (`pullDomainEvents()`) and publishes them to the `EventBus`.
- **Infrastructure**: `SpringApplicationEventBus` uses Spring's native system to propagate them.

### 9. Pure Dependency Injection (Dependency Inversion)
Total decoupling from the framework.
- **Problem**: Using `@Service` or `@Autowired` inside Handlers dirties application code with Spring dependencies.
- **Solution**: Handlers (`CreateCourseCommandHandler`) are pure POJOs (Plain Old Java Objects) without annotations.
- **Configuration**: The `CourseModuleDependencyConfig` class in the infrastructure layer is the only one that knows about Spring and declares `@Bean`s, manually injecting repositories and buses. This allows migrating to another framework (Quarkus, Micronaut) without touching business logic.

### 10. Input Validation (Fail Fast)
Protection of the domain layer from invalid data at the entrance.
- **Bean Validation (JSR-380)**: Use of standard annotations (`@NotBlank`, `@Size`, etc.) in input DTOs (`CourseRequest`).
- **Fail Fast**: The controller rejects invalid requests before they touch the domain or application.
- **Global Error Handling**: `GlobalExceptionHandler` intercepts validation errors (`MethodArgumentNotValidException`) and returns a structured response (JSON with field and error) and HTTP code **400 Bad Request**.

### 11. Advanced Tests
Robust testing pyramid strategy.
- **Architecture Tests (ArchUnit)**: Refined rules ensuring domain isolation but allowing flexibility in infrastructure naming.
- **Event Integration Tests**: `WelcomeEmailIntegrationTest` lifts the Spring context to verify the full async flow: *Event Publication -> Listener -> Use Case -> Port -> Adapter (Mock)*.
- **Serialization Tests**: `DomainEventSerializationTest` ensures domain events can be correctly converted to JSON, simulating a real messaging scenario (Kafka/RabbitMQ).

### 12. Observability (Actuator)
Production-ready features (Cloud/Kubernetes).
- **Health Checks**: Endpoint `/actuator/health` so orchestrators know if the pod is alive.
- **Metrics**: Endpoint `/actuator/metrics` for monitoring (Prometheus, Grafana).

### 13. Modern Java (Records)
Use of Java 21+ features.
- **Records**: DTOs (`CreateCourseCommand`, `CourseResponse`) are `record` instead of `class`. This eliminates boilerplate (getters, equals, hashCode, toString) and makes code more concise and safe (immutability by default).

---

## 📂 Project Structure

Packet structure reflects **Business** (Modules) and not technical layers.

```text
src/main/java/com/hexagonal/demo
├── courses                          <-- BOUNDED CONTEXT MODULE
│   ├── application
│   │   ├── create                   <-- WRITE SIDE
│   │   │   ├── CreateCourseCommand.java
│   │   │   └── CreateCourseCommandHandler.java
│   │   └── find                     <-- READ SIDE
│   │       ├── CourseResponse.java
│   │       ├── FindCourseQueryHandler.java
│   │       └── CourseNotFound.java
│   ├── domain
│   │   ├── Course.java              (Aggregate Root)
│   │   ├── CourseCreatedEvent.java  (Domain Event)
│   │   ├── CourseId.java
│   │   └── CourseRepository.java
│   └── infrastructure
│       ├── api
│       │   └── CoursePostController.java
│       └── persistence
│           └── InMemoryCourseRepository.java
└── shared                           <-- SHARED KERNEL
    ├── domain
    │   ├── AggregateRoot.java
    │   ├── Identifier.java
    │   └── bus/event                (Event Ports)
    └── infrastructure
        └── bus/event/spring         (Spring Adapter)
```

```

### 🗺️ Architecture Map
Visual representation of dependency flow and domain isolation.

```mermaid
graph TD
    subgraph Infrastructure [Infrastructure (Outside)]
        style Infrastructure fill:#ffdfba,stroke:#333,stroke-width:2px
        API[API REST Controller]
        DB[H2 Persistence Adapter]
        EmailAdapter[Fake Email Sender]
    end

    subgraph Application [Application (Orchestration)]
        style Application fill:#ffffba,stroke:#333,stroke-width:2px
        CMD[Create Course Handler]
        QUERY[Find Course Handler]
    end

    subgraph Domain [Domain (Core)]
        style Domain fill:#baffc9,stroke:#333,stroke-width:4px
        Course[Course Entity]
        RepoPort[<<Interface>>\nCourseRepository]
        EmailPort[<<Interface>>\nEmailSender]
    end

    %% Dependency Flow (Outside -> Inside)
    API --> CMD
    API --> QUERY
    CMD --> Course
    CMD --> RepoPort
    QUERY --> RepoPort

    %% Implementation (Inversion of Control)
    DB -.->|implements| RepoPort
    EmailAdapter -.->|implements| EmailPort
    
    %% Events
    Course -- emits --> Event[CourseCreatedEvent]
    Event --> EmailAdapter
```

---

## 🛠️ Execution Flow (Step by Step)

### Write Flow (POST /courses)
1.  **HTTP Request**: User sends a `POST /courses`.
2.  **Input Adapter**: `CoursePostController` -> `CreateCourseCommand`.
3.  **Application Service**: `CreateCourseCommandHandler`.
    1.  Creates `Course`.
    2.  `Course` (Domain) is created and internally **records** `CourseCreatedEvent`.
    3.  Persists in Repository.
    4.  Publishes events to `EventBus`.
4.  **Side Effects (Decoupling)**:
    - `WelcomeEmailSubscriber` listens to `CourseCreatedEvent`.
    - Invokes `SendWelcomeEmail` Use Case (Application).
    - This uses `EmailSender` Port (Domain).
    - Finally, `FakeEmailSender` Adapter (Infra) executes the action (log).

### Read Flow (GET /courses/{id})
1.  **HTTP Request**: Receives `GET /courses/uuid`.
2.  **Query Handler**: `FindCourseQueryHandler` searches in repository.
3.  **Response**:
    - **If exists**: Converts `Course` -> `CourseResponse` (DTO) and returns 200 OK.
    - **If not exists**: Throws `CourseNotFound`. `GlobalExceptionHandler` captures it and returns 404.

### 14. Developer Experience (Makefile & HTTP Client)
To simplify development:
- **Makefile**: Shortcuts for common commands (`make run`, `make test`).
- **requests.http**: Executable file in IntelliJ/VSCode to test API without leaving the editor.

```bash
make help          # See all commands
```

### 15. Swagger UI (OpenAPI)
Living API documentation.
- Accessible at `http://localhost:8080/swagger-ui.html` when app starts.
- Allows testing endpoints visually.

---

## 🚀 How to Test the Application

1.  **Start**:
    ```bash
    ./mvnw spring-boot:run
    ```

2.  **Check Health (Actuator)**:
    ```bash
    curl http://localhost:8080/actuator/health
    # Response: {"status":"UP"}
    ```

3.  **Validation Test (Fail Fast)**:
    Try to create a course with a short name:
    ```bash
    curl -X POST http://localhost:8080/courses \
         -H "Content-Type: application/json" \
         -d '{"id": "valid-uuid", "name": "Hi", "duration": "15m"}'
    ```
    *You will receive a 400 Bad Request with validation error details.*

4.  **Create a Course (Write + Event)**:
    ```bash
    curl -X POST http://localhost:8080/courses \
         -H "Content-Type: application/json" \
         -d '{"id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", "name": "Hexagonal Master", "duration": "15m"}'
    ```
    *You will see in console that the course is saved AND the `WelcomeEmailSubscriber` log simulating email sending.*

5.  **Search Preloaded Course (Read)**:
    The system automatically preloads 2 example courses at startup. You can query them directly:
    ```bash
    # Course 1: Hexagonal Architecture Masterclass
    curl http://localhost:8080/courses/1a9b456b-e85b-4b2a-a92c-d9a2c6d4838f
    
    # Course 2: Domain-Driven Design Tactical Patterns
    curl http://localhost:8080/courses/2b9b456b-e85b-4b2a-a92c-d9a2c6d4838f
    ```

    > **💡 Tip**: You can also create your own course with the command in step 4 and then find it by its UUID.
    
    > **⚠️ Important**: IDs must be valid UUIDs (format: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`). 
    > If you try to use a simple ID like `1` or `abc`, you will receive a validation error.

### 🐳 Deployment with Docker
The application includes an optimized multi-stage `Dockerfile`.

1.  **Build the image**:
    ```bash
    docker build -t hexagonal-architecture-masterclass .
    ```

2.  **Run the container**:
    ```bash
    docker run -p 8080:8080 hexagonal-architecture-masterclass
    ```
    *API will be available at `http://localhost:8080`*.

### 15. Real Database (JPA + H2)
Demonstration of adapter change (Persistence) without affecting the domain.
- **Implementation**: `H2CourseRepository` uses `JpaRepository` and maps `CourseEntity` (infrastructure) to `Course` (domain).
- **H2 Console**: Accessible at `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - User: `sa`
    - Password: (empty)

### 16. Orchestration (Microservice + PostgreSQL)
To demonstrate real portability of hexagonal architecture, we include a `docker-compose.yml`.
It spins up:
1.  **PostgreSQL 15**: Real production database.
2.  **Hexagonal App**: Configured (via env vars) to connect to Postgres instead of H2.

```bash
make compose-up
```
*This demonstrates that Domain layer DOES NOT change even if we change infrastructure from H2 (Memory) to Postgres (Disk).*

### 17. Architecture Decisions (ADRs)
We document the "why" of our technical decisions using ADR format.
- [001 - Adopt Hexagonal Architecture](docs/adr/001-adoptar-arquitectura-hexagonal.md)

### 18. Code Quality (Coverage)
Project includes **JaCoCo** to measure test coverage.
```bash
make coverage
```
*This will run tests and automatically open a detailed web report in your browser.*

### 19. Async Events (Performance)
To demonstrate real decoupling, we have configured:
- **`@Async`**: Email sending happens in a separate thread.
- **Simulated Latency**: `FakeEmailSender` has an intentional `Thread.sleep(2000)`.
- **Result**: Even though sending email takes 2 seconds, HTTP API responds in milliseconds (`201 Created`). User doesn't wait.

### 20. Performance Optimization
Project includes optimizations to improve startup time:
- **Lazy Initialization**: Beans initialized only when needed
- **JMX Disabled**: Reduces overhead in development
- **JPA Optimized**: `open-in-view=false` to avoid lazy loading issues
- **Specific Component Scan**: Only scans necessary packages

**Result**: Significantly reduced startup time without compromising functionality.

---

## 📚 References and Recommended Reading

To deepen these concepts, here are some of the best resources:

### 🏰 Hexagonal Architecture (Ports & Adapters)
- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) (Original source)
- [Herberto Graça - Ports & Adapters Architecture](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/) (Excellent visual explanation)

### 📘 DDD (Domain-Driven Design)
- [Martin Fowler - Domain Driven Design](https://martinfowler.com/tags/domain%20driven%20design.html)
- [Domain-Driven Design Reference (Eric Evans)](https://www.domainlanguage.com/ddd/reference/) (Official free summary)

### ✨ Clean Code & SOLID
- [Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
- [Refactoring.guru - Clean Code & Design Patterns](https://refactoring.guru/es/refactoring/technical-debt)

### 🔄 CQRS & Event Sourcing
- [Martin Fowler - CQRS](https://martinfowler.com/bliki/CQRS.html)
- [Microsoft - CQRS Pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

### 🧪 TDD (Test-Driven Development)
- [Kent Beck - Test Driven Development: By Example](https://www.amazon.com/Test-Driven-Development-Kent-Beck/dp/0321146530)
- [Martin Fowler - TDD](https://martinfowler.com/bliki/TestDrivenDevelopment.html)

### 📡 Domain Events
- [Vaughn Vernon - Domain Events](https://kalele.io/blog-posts/domain-events-salvation/)
- [Spring Events - Baeldung](https://www.baeldung.com/spring-events) (Technical implementation in Spring)

---

## ✅ How to Run Tests

```bash
# Run all tests
./mvnw test
```

**15 tests** are executed validating from pure domain logic to event integration and architecture rules.

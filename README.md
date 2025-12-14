# Ejemplo Didáctico: Arquitectura Hexagonal con Spring Boot

Este proyecto es un ejemplo práctico y didáctico diseñado para explicar cómo integrar conceptos avanzados de ingeniería de software en una aplicación Java con Spring Boot.

## 🚀 Conceptos Implementados

### 1. DDD (Domain-Driven Design)
El núcleo del software es el **Dominio** (la lógica de negocio), y debe estar aislado de detalles técnicos.
- **Entidades Ricas**: `Course` no es solo 'datos', contiene lógica y validaciones.
- **Value Objects**: `CourseId` y `CourseName`. Evitamos usar tipos primitivos (`String`, `int`) para conceptos de dominio. Esto previene errores (ej. pasar un nombre donde se espera un ID) y encapsula reglas de validación.

### 2. Arquitectura Hexagonal (Ports & Adapters)
Divide la aplicación en **Interior** (Dominio + Aplicación) y **Exterior** (Infraestructura).
- **Puertos (Ports)**: Interfaces definidas en el dominio (`CourseRepository`). El dominio dice *qué* necesita, pero no *cómo* se hace.
- **Adaptadores (Adapters)**: Implementaciones en la capa de infraestructura.
    - **Driver (Entrada)**: `CoursePostController` (API REST).
    - **Driven (Salida)**: `InMemoryCourseRepository` (Base de datos).

### 3. Clean Code
Código legible y mantenible.
- **Nombres Expresivos**: Las clases y métodos dicen exactamente qué hacen.
- **Métodos Cortos**: Responsabilidad única (SOLID).
- **Constructores Semánticos**: Uso de métodos de fabricación estáticos (`Course.create(...)`) en lugar de constructores complejos públicos.

### 4. CQRS (Command Query Responsibility Segregation)
Separación de operaciones de Escritura (Command) y Lectura (Query).
- **Write Side (Comando)**: Optimizado para consistencia y reglas de negocio.
    - `CreateCourseCommand`: Intención de usuario.
    - `CreateCourseCommandHandler`: Lógica que modifica el estado.
- **Read Side (Query)**: Optimizado para la vista del cliente.
    - `FindCourseQueryHandler`: Busca datos y devuelve DTOs planos (`CourseResponse`). Nunca exponemos la Entidad de Dominio directamente en la lectura para no acoplar la API a las reglas internas.

### 5. Shared Kernel (Núcleo Compartido)
Código reutilizable entre distintos Bounded Contexts (módulos).
- **Identifier**: Clase base abstracta (`shared.domain.Identifier`) que encapsula la lógica y validación de UUIDs. `CourseId` hereda de ella, evitando duplicación de código si mañana creamos `StudentId`.

### 6. Manejo Global de Errores (Error Handling)
Transformación de excepciones de dominio en respuestas HTTP coherentes.
- **GlobalExceptionHandler**: Usa `@ControllerAdvice` de Spring para capturar excepciones:
    - `IllegalArgumentException` (Validación) -> **400 Bad Request**.
    - `CourseNotFound` (No existe) -> **404 Not Found**.

### 7. TDD (Test-Driven Development)
Desarrollo guiado por pruebas en todos los niveles.
- **Unitarios Dominio**: `CourseNameTest` y `CourseIdTest` blindan las reglas de negocio (validaciones, nulos...).
- **Unitarios Aplicación**: `CreateCourseCommandHandlerTest` verifica la orquestación (mocks).
- **Integración API**: `CoursePostControllerTest` verifica la capa web HTTP simulando peticiones reales.

### 8. Eventos de Dominio (Domain Events)
Mecanismo para desacoplar efectos secundarios (enviar email, logs, analytics).
- **Core**: `AggregateRoot` (Shared Kernel) permite a las entidades registrar qué ha pasado (`record()`).
- **Evento**: `CourseCreatedEvent` captura que un curso fue creado.
- **Publicación**: El Handler recupera los eventos del agregado (`pullDomainEvents()`) y los publica en el `EventBus`.
- **Infraestructura**: `SpringApplicationEventBus` usa el sistema nativo de Spring para propagarlos.

### 9. Inyección de Dependencias Pura (Dependency Inversion)
Desacoplamiento total del framework.
- **Problema**: Usar `@Service` o `@Autowired` dentro de los Handlers ensucia el código de aplicación con dependencias de Spring.
- **Solución**: Los Handlers (`CreateCourseCommandHandler`) son POJOs puros (Plain Old Java Objects) sin anotaciones.
- **Configuración**: La clase `CourseModuleDependencyConfig` en la capa de infraestructura es la única que sabe de Spring y declara los `@Bean`, inyectando manualmente repositorios y buses. Esto permite migrar a otro framework (Quarkus, Micronaut) sin tocar la lógica de negocio.

### 10. Validación de Entrada (Fail Fast)
Protección de la capa de dominio ante datos inválidos desde la entrada.
- **Bean Validation (JSR-380)**: Uso de anotaciones estándar (`@NotBlank`, `@Size`, etc.) en los DTOs de entrada (`CourseRequest`).
- **Fail Fast**: El controlador rechaza peticiones inválidas antes de que toquen el dominio o la aplicación.
- **Global Error Handling**: `GlobalExceptionHandler` intercepta los errores de validación (`MethodArgumentNotValidException`) y devuelve una respuesta estructurada (JSON con campo y error) y código HTTP **400 Bad Request**.

### 11. Tests Avanzados
Estrategia de testing piramidal robusta.
- **Tests de Arquitectura (ArchUnit)**: Reglas refinadas que aseguran el aislamiento del dominio pero permiten flexibilidad en nombres de clases auxiliares de infraestructura.
- **Tests de Integración de Eventos**: `WelcomeEmailIntegrationTest` levanta el contexto de Spring para verificar el flujo asíncrono completo: *Publicación Evento -> Listener -> Caso de Uso -> Puerto -> Adaptador (Mock)*.
- **Tests de Serialización**: `DomainEventSerializationTest` asegura que los eventos de dominio se pueden convertir a JSON correctamente, simulando un escenario real de mensajería (Kafka/RabbitMQ).

### 12. Observabilidad (Actuator)
Características listas para producción (Cloud/Kubernetes).
- **Health Checks**: Endpoint `/actuator/health` para que orquestadores sepan si el pod está vivo.
- **Metrics**: Endpoint `/actuator/metrics` para monitorización (Prometheus, Grafana).

### 13. Java Moderno (Records)
Uso de características de Java 17+.
- **Records**: Los DTOs (`CreateCourseCommand`, `CourseResponse`) son `record` en lugar de `class`. Esto elimina boilerplate (getters, equals, hashCode, toString) y hace el código más conciso y seguro (inmutabilidad por defecto).

---

## 📂 Estructura del Proyecto

La estructura de paquetes refleja el **Negocio** (Módulos) y no las capas técnicas.

```text
src/main/java/com/hexagonal/demo
├── courses                          <-- MÓDULO BOUNDED CONTEXT
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
└── shared                           <-- KERNEL COMPARTIDO
    ├── domain
    │   ├── AggregateRoot.java
    │   ├── Identifier.java
    │   └── bus/event                (Puertos de Eventos)
    └── infrastructure
        └── bus/event/spring         (Adaptador Spring)
```

```

### 🗺️ Mapa de Arquitectura
Representación visual del flujo de dependencias y aislamiento del dominio.

```mermaid
graph TD
    subgraph Infrastructure [Infraestructura (Exterior)]
        style Infrastructure fill:#ffdfba,stroke:#333,stroke-width:2px
        API[API REST Controller]
        DB[H2 Persistence Adapter]
        EmailAdapter[Fake Email Sender]
    end

    subgraph Application [Aplicación (Orquestación)]
        style Application fill:#ffffba,stroke:#333,stroke-width:2px
        CMD[Create Course Handler]
        QUERY[Find Course Handler]
    end

    subgraph Domain [Dominio (Núcleo)]
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

## 🛠️ Flujo de Ejecución (Paso a Paso)

### Flujo de Escritura (POST /courses)
1.  **Petición HTTP**: El usuario envía un `POST /courses`.
2.  **Adaptador de Entrada**: `CoursePostController` -> `CreateCourseCommand`.
3.  **Application Service**: `CreateCourseCommandHandler`.
    1.  Crea `Course`.
    2.  `Course` (Dominio) se crea y **registra** internamente `CourseCreatedEvent`.
    3.  Persiste en Repositorio.
    4.  Publica eventos en `EventBus`.
4.  **Efectos Secundarios (Desacoplamiento)**:
    - `WelcomeEmailSubscriber` escucha el `CourseCreatedEvent`.
    - Invoca al Caso de Uso `SendWelcomeEmail` (Application).
    - Este usa el Puerto `EmailSender` (Domain).
    - Finalmente, el Adaptador `FakeEmailSender` (Infra) ejecuta la acción (log).

### Flujo de Lectura (GET /courses/{id})
1.  **Petición HTTP**: Recibe `GET /courses/uuid`.
2.  **Query Handler**: `FindCourseQueryHandler` busca en el repositorio.
3.  **Respuesta**:
    - **Si existe**: Convierte `Course` -> `CourseResponse` (DTO) y devuelve 200 OK.
    - **Si no existe**: Lanza `CourseNotFound`. El `GlobalExceptionHandler` la captura y devuelve 404.

### 14. Developer Experience (Makefile & HTTP Client)
Para simplificar el desarrollo:
- **Makefile**: Atajos para comandos comunes (`make run`, `make test`).
- **requests.http**: Archivo ejecutable en IntelliJ/VSCode para probar la API sin salir del editor.

```bash
make help          # Ver todos los comandos
```

### 15. Swagger UI (OpenAPI)
Documentación viva de la API.
- Accesible en `http://localhost:8080/swagger-ui.html` al arrancar la app.
- Permite probar los endpoints visualmente.

---

## 🚀 Cómo Probar la Aplicación

1.  **Arrancar**:
    ```bash
    ./mvnw spring-boot:run
    ```

2.  **Verificar Salud (Actuator)**:
    ```bash
    curl http://localhost:8080/actuator/health
    # Respuesta: {"status":"UP"}
    ```

3.  **Prueba de Validación (Fail Fast)**:
    Intenta crear un curso con nombre corto:
    ```bash
    curl -X POST http://localhost:8080/courses \
         -H "Content-Type: application/json" \
         -d '{"id": "uuid-valido", "name": "Hi", "duration": "15m"}'
    ```
    *Recibirás un 400 Bad Request con el detalle del error de validación.*

4.  **Crear un Curso (Escritura + Evento)**:
    ```bash
    curl -X POST http://localhost:8080/courses \
         -H "Content-Type: application/json" \
         -d '{"id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", "name": "Hexagonal Master", "duration": "15m"}'
    ```
    *Verás en la consola que se guarda el curso Y ADEMÁS salta el log del `WelcomeEmailSubscriber` simulan el envío de email.*

5.  **Buscar el Curso (Lectura)**:
    ```bash
    curl http://localhost:8080/courses/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11
    ```

    > **Nota**: Al arrancar, el sistema precarga automáticamente dos cursos de ejemplo. Puedes probar a buscarlos usando estos IDs:
    > - `1a9b456b-e85b-4b2a-a92c-d9a2c6d4838f`
    > - `2b9b456b-e85b-4b2a-a92c-d9a2c6d4838f`

### 🐳 Despliegue con Docker
La aplicación incluye un `Dockerfile` multi-stage optimizado.

1.  **Construir la imagen**:
    ```bash
    docker build -t hexagonal-demo .
    ```

2.  **Ejecutar el contenedor**:
    ```bash
    docker run -p 8080:8080 hexagonal-demo
    ```
    *La API estará disponible en `http://localhost:8080`*.

### 15. Base de Datos Real (JPA + H2)
Demostración de cambio de adaptador (Persistencia) sin afectar al dominio.
- **Implementación**: `H2CourseRepository` usa `JpaRepository` y mapea la entidad `CourseEntity` (infraestructura) hacia `Course` (dominio).
- **Consola H2**: Accesible en `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - User: `sa`
    - Password: (vacío)

### 16. Orquestación (Microservicio + PostgreSQL)
Para demostrar la portabilidad real de la arquitectura hexagonal, incluimos un `docker-compose.yml`.
Este levanta:
1.  **PostgreSQL 15**: Base de datos de producción real.
2.  **Hexagonal App**: Configurada (via variables de entorno) para conectarse a Postgres en lugar de H2.

```bash
make compose-up
```
*Esto demuestra que la capa de Dominio NO cambia aunque cambiemos la infraestructura de H2 (Memoria) a Postgres (Disco).*

### 17. Decisiones de Arquitectura (ADRs)
Documentamos el "por qué" de nuestras decisiones técnicas usando el formato ADR.
- [001 - Adoptar Arquitectura Hexagonal](docs/adr/001-adoptar-arquitectura-hexagonal.md)

### 18. Calidad de Código (Coverage)
El proyecto incluye **JaCoCo** para medir la cobertura de los tests.
```bash
make coverage
```
*Esto ejecutará los tests y abrirá automáticamente un reporte web detallado en tu navegador.*

### 19. Eventos Asíncronos (Performance)
Para demostrar el desacoplamiento real, hemos configurado:
- **`@Async`**: El envío de email ocurre en un hilo separado.
- **Latencia Simulada**: El `FakeEmailSender` tiene un `Thread.sleep(2000)` intencional.
- **Resultado**: Aunque enviar el email tarda 2 segundos, la API HTTP responde en milisegundos (`201 Created`). El usuario no espera.

---

## 📚 Referencias y Lecturas Recomendadas

Para profundizar en estos conceptos, aquí tienes una selección de los mejores recursos:

### 🏰 Arquitectura Hexagonal (Ports & Adapters)
- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) (Fuente original)
- [Herberto Graça - Ports & Adapters Architecture](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/) (Excelente explicación visual)

### 📘 DDD (Domain-Driven Design)
- [Martin Fowler - Domain Driven Design](https://martinfowler.com/tags/domain%20driven%20design.html)
- [Domain-Driven Design Reference (Eric Evans)](https://www.domainlanguage.com/ddd/reference/) (Resumen oficial gratuito)

### ✨ Clean Code & SOLID
- [Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
- [Refactoring.guru - Clean Code & Design Patterns](https://refactoring.guru/es/refactoring/technical-debt)

### 🔄 CQRS & Event Sourcing
- [Martin Fowler - CQRS](https://martinfowler.com/bliki/CQRS.html)
- [Microsoft - CQRS Pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

### 🧪 TDD (Test-Driven Development)
- [Kent Beck - Test Driven Development: By Example](https://www.amazon.com/Test-Driven-Development-Kent-Beck/dp/0321146530)
- [Martin Fowler - TDD](https://martinfowler.com/bliki/TestDrivenDevelopment.html)

### 📡 Eventos de Dominio
- [Vaughn Vernon - Domain Events](https://kalele.io/blog-posts/domain-events-salvation/)
- [Spring Events - Baeldung](https://www.baeldung.com/spring-events) (Implementación técnica en Spring)

---

## ✅ Cómo ejecutar los Tests

```bash
# Ejecutar todos los tests
Antes de Java 24: ./mvn test
En Java 24: ./mvn test -Dnet.bytebuddy.experimental=true
```

Se ejecutan **15 tests** que validan desde la lógica pura del dominio hasta la integración de eventos y la propia arquitectura del código.

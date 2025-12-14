# AI Agents Guide

Este documento proporciona instrucciones específicas para agentes de IA que trabajen con este proyecto.

## 🤖 Contexto del Proyecto

Este es un proyecto de **referencia educativa** que implementa Arquitectura Hexagonal con las mejores prácticas de ingeniería de software. No es un proyecto de producción activo, sino un ejemplo didáctico completo.

## 📋 Comandos Principales

### Desarrollo Local
```bash
make run          # Arrancar aplicación (H2 en memoria)
make test         # Ejecutar todos los tests
make coverage     # Generar reporte de cobertura
make health       # Verificar salud de la aplicación
```

### Tests (Java 24)
```bash
mvn test -Dnet.bytebuddy.experimental=true
```

### Docker
```bash
make docker-build    # Construir imagen
make docker-run      # Ejecutar contenedor
make compose-up      # Levantar stack completo (App + PostgreSQL)
make compose-down    # Destruir stack
```

## 🏗️ Estructura de Arquitectura

### Reglas Fundamentales (Verificadas por ArchUnit)
1. **Dominio NO puede depender de Aplicación ni Infraestructura**
2. **Aplicación NO puede depender de Infraestructura**
3. **Controladores deben terminar en `Controller`**
4. **Value Objects (`*Id`, `*Name`) deben estar en `..domain..`**

### Flujo de Dependencias
```
Infrastructure (API, DB) 
    ↓ depende de
Application (Use Cases)
    ↓ depende de
Domain (Entities, Value Objects, Ports)
```

## 🔧 Modificaciones Comunes

### Añadir un Nuevo Caso de Uso
1. Crear Command/Query en `application/`
2. Crear Handler correspondiente
3. Inyectar en `*ModuleDependencyConfig`
4. Crear test unitario del Handler
5. Actualizar Controller si es necesario

### Añadir un Nuevo Value Object
1. Crear clase en `domain/` extendiendo `Identifier` o creando nueva
2. Añadir validaciones en el constructor
3. Crear test unitario en `domain/`

### Añadir un Nuevo Evento
1. Crear evento extendiendo `DomainEvent` en `domain/`
2. Registrar evento en el Aggregate (`record()`)
3. Crear Subscriber en `infrastructure/`
4. Marcar Subscriber con `@Async` si es asíncrono
5. Crear test de integración

## 🧪 Testing

### Pirámide de Tests
- **Unitarios**: Domain (Value Objects, Entities)
- **Integración**: Application (Handlers con mocks)
- **E2E**: Infrastructure (Controllers con MockMvc)
- **Arquitectura**: ArchUnit (Reglas de dependencias)

### Cobertura Esperada
- Dominio: ~100%
- Aplicación: ~90%
- Infraestructura: ~70%

## 📦 Módulos del Proyecto

### `courses`
Bounded Context principal que gestiona cursos.
- **Dominio**: `Course`, `CourseId`, `CourseName`, `CourseCreatedEvent`
- **Aplicación**: `CreateCourseCommand`, `FindCourseQuery`
- **Infraestructura**: `CoursePostController`, `H2CourseRepository`

### `notifications`
Bounded Context de notificaciones (ejemplo de desacoplamiento).
- **Dominio**: `EmailSender` (puerto)
- **Aplicación**: `SendWelcomeEmail`
- **Infraestructura**: `FakeEmailSender`, `WelcomeEmailSubscriber`

### `shared`
Kernel compartido entre módulos.
- **Dominio**: `AggregateRoot`, `Identifier`, `DomainEvent`, `EventBus`
- **Infraestructura**: `SpringApplicationEventBus`

## 🚨 Restricciones Importantes

### NO Hacer
- ❌ Añadir `@Service` o `@Component` en clases de `domain/` o `application/`
- ❌ Importar clases de Spring en `domain/`
- ❌ Usar `System.out.println` (usar SLF4J Logger)
- ❌ Exponer entidades de dominio directamente en Controllers
- ❌ Mezclar lógica de negocio en Controllers

### SÍ Hacer
- ✅ Usar DTOs (Commands, Queries, Responses) en la API
- ✅ Mantener el dominio puro (POJOs)
- ✅ Inyectar dependencias manualmente en `*Config`
- ✅ Usar Records para DTOs inmutables
- ✅ Escribir tests ANTES de implementar (TDD)

## 🔄 Workflow de Cambios

1. **Crear rama**: `git checkout -b feature/nueva-funcionalidad`
2. **Escribir test**: Crear test que falle (Red)
3. **Implementar**: Hacer que el test pase (Green)
4. **Refactorizar**: Mejorar código sin romper tests (Refactor)
5. **Verificar arquitectura**: `mvn test` (incluye ArchUnit)
6. **Commit**: `git commit -m "feat: descripción"`
7. **Push**: CI ejecutará tests automáticamente

## 📊 Métricas de Calidad

### Indicadores Clave
- **Tests**: 15 tests pasando
- **Cobertura**: >80% (verificar con `make coverage`)
- **Arquitectura**: 0 violaciones de ArchUnit
- **Build**: Debe pasar en CI (GitHub Actions)

## 🔍 Debugging

### Logs Importantes
- **Eventos**: Buscar `WelcomeEmailSubscriber` en logs
- **Persistencia**: Buscar `InMemoryCourseRepository` o `H2CourseRepository`
- **Validación**: Buscar `GlobalExceptionHandler`

### Endpoints Útiles
- `GET /actuator/health` - Estado de la aplicación
- `GET /actuator/info` - Información de Git y build
- `GET /actuator/metrics` - Métricas de rendimiento
- `GET /swagger-ui.html` - Documentación interactiva de la API

## 🎯 Objetivos de Diseño

Este proyecto demuestra:
1. **Independencia del Framework**: El dominio no conoce Spring
2. **Testabilidad**: Tests rápidos sin contexto completo
3. **Flexibilidad**: Cambiar DB sin tocar dominio
4. **Escalabilidad**: Eventos asíncronos para desacoplamiento
5. **Mantenibilidad**: Código limpio y bien documentado

## 📚 Recursos de Referencia

- **ADRs**: Ver `docs/adr/` para decisiones de arquitectura
- **README**: Documentación completa del proyecto
- **Mermaid Diagram**: Visualización de la arquitectura en README
- **requests.http**: Ejemplos de uso de la API

## 🤝 Contribuciones de Agentes

Si eres un agente de IA contribuyendo a este proyecto:
1. Respeta las reglas de arquitectura hexagonal
2. Mantén la cobertura de tests alta
3. Documenta decisiones importantes en ADRs
4. Usa commits semánticos (feat, fix, docs, refactor)
5. Verifica que `mvn test` pase antes de commit

---

**Nota**: Este es un proyecto educativo. Prioriza la claridad y el aprendizaje sobre la optimización prematura.

# 1. Adoptar Arquitectura Hexagonal

Fecha: 2025-12-14

> 🇬🇧 [English Version](001-adopt-hexagonal-architecture.md)

## Estado
Aceptado

## Contexto
El proyecto requiere una lógica de negocio rica que sea independiente de frameworks, bases de datos y interfaces de usuario externas. Queremos garantizar que el núcleo del dominio pueda evolucionar sin verse afectado por cambios en la infraestructura (ej. cambiar de H2 a PostgreSQL, o de REST a GraphQL).

## Decisión
Adoptaremos la **Arquitectura Hexagonal (Ports & Adapters)**.

## Consecuencias

### Positivas
*   **Desacoplamiento**: El dominio es puro Java y no conoce a Spring Boot ni a la base de datos.
*   **Testabilidad**: Podemos probar toda la lógica de negocio con tests unitarios rápidos, sin levantar contextos de Spring.
*   **Flexibilidad**: Podemos cambiar el adaptador de persistencia (Infrastructure) sin tocar una línea del dominio.

### Negativas
*   **Complejidad Inicial**: Requiere crear más clases (Interfaces/Puertos, Adaptadores, DTOs) que una arquitectura MVC tradicional de 3 capas.
*   **Mapeo de Datos**: Necesidad de duplicar modelos de datos (Entidad de Dominio vs Entidad JPA) y crear convertidores.

## Cumplimiento
*   Las clases de dominio no deben tener `import org.springframework...`.
*   Toda comunicación con el exterior debe hacerse a través de interfaces (Puertos).
*   Se usarán tests de arquitectura (ArchUnit) para garantizar estas reglas automáticamente.

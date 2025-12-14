package com.futesat.hexagonal.courses.domain;

import com.futesat.hexagonal.shared.domain.AggregateRoot;
import java.util.Objects;

// Entidad de Dominio (Aggregate Root)
// Representa el concepto de negocio, agnóstico de BBDD.
public class Course extends AggregateRoot {
    private final CourseId id;
    private final CourseName name;
    private final String duration; // Por simplicidad lo dejamos String por ahora

    // Constructor privado o de paquete para forzar el uso de métodos de creación
    // (Factory Method)
    private Course(CourseId id, CourseName name, String duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    // Factory Method estático (Clean Code: Expresividad)
    public static Course create(CourseId id, CourseName name, String duration) {
        Course course = new Course(id, name, duration);

        // ¡Magia DDD! Registramos que ha pasado algo importante.
        course.record(new CourseCreatedEvent(id.getValue(), name.getValue(), duration));

        return course;
    }

    // Método para rehidratar desde infraestructura (BBDD) sin disparar eventos
    public static Course from(String id, String name, String duration) {
        return new Course(
                new CourseId(id),
                new CourseName(name),
                duration);
    }

    public CourseId id() {
        return id;
    }

    public CourseName name() {
        return name;
    }

    public String duration() {
        return duration;
    }

    // Lógica de negocio iría aquí (ej. rename())

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id); // Las entidades se comparan por ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

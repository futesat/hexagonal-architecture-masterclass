package com.futesat.hexagonal.courses.application.find;

import com.futesat.hexagonal.courses.domain.Course;

import java.io.Serializable;

// Response DTO. Maps what we want to return to the client.
// DTO de respuesta. Mapea lo que queremos devolver al cliente.
public record CourseResponse(String id, String name, String duration) implements Serializable {

    // Static convenience method to convert from Domain -> DTO
    // Método estático de conveniencia para convertir desde Dominio -> DTO
    public static CourseResponse fromAggregate(Course course) {
        return new CourseResponse(
                course.id().getValue(),
                course.name().getValue(),
                course.duration());
    }
}

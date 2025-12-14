package com.futesat.hexagonal.courses.application.create;

// Immutable DTO representing the intention to create a course.
// DTO inmutable que representa la intención de crear un curso.
// In modern Java we could use records (Java 14+), but we use class for clarity in older versions if applicable,
// En Java moderno podríamos usar records (Java 14+), pero usaremos class por claridad en versiones anteriores si fuera el caso,
// although here we use a standard class with getters.
// aunque aquí usaremos una clase standard con getters.

public record CreateCourseCommand(String id, String name, String duration) {
}

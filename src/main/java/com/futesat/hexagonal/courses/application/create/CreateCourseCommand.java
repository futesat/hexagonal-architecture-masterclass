package com.futesat.hexagonal.courses.application.create;

// DTO inmutable que representa la intención de crear un curso.
// En Java moderno podríamos usar records (Java 14+), pero usaremos class por claridad en versiones anteriores si fuera el caso,
// aunque aquí usaremos una clase standard con getters.

public record CreateCourseCommand(String id, String name, String duration) {
}

package com.futesat.hexagonal.courses.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseIdTest {

    @Test
    void should_create_valid_course_id() {
        assertDoesNotThrow(() -> new CourseId("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"));
    }

    @Test
    void should_throw_exception_when_id_is_not_valid_uuid() {
        assertThrows(IllegalArgumentException.class, () -> new CourseId("invalid-uuid"));
    }

    @Test
    void should_throw_exception_when_id_is_null() {
        // Identifier lanza excepción al intentar hacer UUID.fromString(null) o valida
        // antes
        assertThrows(IllegalArgumentException.class, () -> new CourseId(null));
    }
}

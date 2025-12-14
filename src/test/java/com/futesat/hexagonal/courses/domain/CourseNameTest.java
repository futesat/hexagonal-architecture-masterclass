package com.futesat.hexagonal.courses.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseNameTest {

    @Test
    void should_create_valid_course_name() {
        assertDoesNotThrow(() -> new CourseName("Valid Name"));
    }

    @Test
    void should_throw_exception_when_name_is_too_short() {
        assertThrows(IllegalArgumentException.class, () -> new CourseName("Test"));
    }

    @Test
    void should_throw_exception_when_name_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new CourseName(null));
    }
}

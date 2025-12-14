package com.futesat.hexagonal.courses.application.find;

import com.futesat.hexagonal.courses.domain.Course;
import com.futesat.hexagonal.courses.domain.CourseId;

import com.futesat.hexagonal.courses.domain.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindCourseQueryHandlerTest {

    private CourseRepository repository;
    private FindCourseQueryHandler handler;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CourseRepository.class);
        handler = new FindCourseQueryHandler(repository);
    }

    @Test
    void should_find_an_existing_course() {
        // GIVEN
        String idSource = "5a02e5b0-394c-4235-8656-78225586618e";
        String nameSource = "Refactoring Legacy Code";
        String durationSource = "15 hours";

        Course existingCourse = Course.from(idSource, nameSource, durationSource);
        when(repository.search(new CourseId(idSource))).thenReturn(Optional.of(existingCourse));

        // WHEN
        CourseResponse response = handler.handle(idSource);

        // THEN
        assertNotNull(response);
        assertEquals(idSource, response.id());
        assertEquals(nameSource, response.name());
        assertEquals(durationSource, response.duration());
    }

    @Test
    void should_throw_exception_when_course_does_not_exist() {
        // GIVEN
        String idSource = "5a02e5b0-394c-4235-8656-78225586618e";
        when(repository.search(new CourseId(idSource))).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(CourseNotFound.class, () -> handler.handle(idSource));
    }
}

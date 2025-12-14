package com.futesat.hexagonal.courses.application.create;

import com.futesat.hexagonal.courses.domain.Course;
import com.futesat.hexagonal.courses.domain.CourseId;
import com.futesat.hexagonal.courses.domain.CourseName;
import com.futesat.hexagonal.courses.domain.CourseRepository;
import com.futesat.hexagonal.shared.domain.bus.event.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CreateCourseCommandHandlerTest {

    private CourseRepository repository;
    private EventBus eventBus;
    private CreateCourseCommandHandler handler;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CourseRepository.class);
        eventBus = Mockito.mock(EventBus.class);
        handler = new CreateCourseCommandHandler(repository, eventBus);
    }

    @Test
    void should_create_a_valid_course() {
        // GIVEN
        String id = "5a02e5b0-394c-4235-8656-78225586618e";
        String name = "DDD in Java";
        String duration = "10 hours";
        CreateCourseCommand command = new CreateCourseCommand(id, name, duration);

        // WHEN
        handler.handle(command);

        // THEN
        Course expectedCourse = Course.create(
                new CourseId(id),
                new CourseName(name),
                duration);

        // Verify save
        // Verificamos guardado
        verify(repository, times(1)).save(refEq(expectedCourse, "domainEvents"));
        // We ignore "domainEvents" in comparison because calling create() twice
        // will produce different event objects (different UUID/time).
        // Ignoramos "domainEvents" en la comparación porque al llamar a create() 2
        // veces,
        // tendrán objetos eventos distintos (distinto UUID/hora).

        // Verify event publication
        // Verificamos publicación de eventos
        verify(eventBus, times(1)).publish(anyList());
    }
}

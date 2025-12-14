package com.futesat.hexagonal.courses.application.create;

import com.futesat.hexagonal.courses.domain.Course;
import com.futesat.hexagonal.courses.domain.CourseId;
import com.futesat.hexagonal.courses.domain.CourseName;
import com.futesat.hexagonal.courses.domain.CourseRepository;
import com.futesat.hexagonal.shared.domain.bus.event.DomainEvent;
import com.futesat.hexagonal.shared.domain.bus.event.EventBus;

import java.util.List;

public class CreateCourseCommandHandler {

    private final CourseRepository repository;
    private final EventBus eventBus;

    public CreateCourseCommandHandler(CourseRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    public void handle(CreateCourseCommand command) {
        // 1. Convert primitive data to Value Objects
        // 1. Convertir datos primitivos a Value Objects
        CourseId id = new CourseId(command.id());
        CourseName name = new CourseName(command.name());
        String duration = command.duration();

        // 2. Create the Aggregate (event is registered internally here)
        // 2. Crear el Agregado (aquí se registra el evento internamente)
        Course course = Course.create(id, name, duration);

        // 3. Persist using the port
        // 3. Persistir usando el puerto
        repository.save(course);

        // 4. Publish domain events
        // 4. Publicar eventos de dominio
        List<DomainEvent> events = course.pullDomainEvents();
        eventBus.publish(events);
    }
}

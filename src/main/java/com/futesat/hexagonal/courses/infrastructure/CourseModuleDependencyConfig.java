package com.futesat.hexagonal.courses.infrastructure;

import com.futesat.hexagonal.courses.application.create.CreateCourseCommandHandler;
import com.futesat.hexagonal.courses.application.find.FindCourseQueryHandler;
import com.futesat.hexagonal.courses.infrastructure.persistence.h2.H2CourseRepository;
import com.futesat.hexagonal.shared.domain.bus.event.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseModuleDependencyConfig {

    // Enseñamos a Spring cómo crear el Handler de CREAR
    @Bean
    public CreateCourseCommandHandler createCourseCommandHandler(H2CourseRepository repository, EventBus eventBus) {
        return new CreateCourseCommandHandler(repository, eventBus);
    }

    // Enseñamos a Spring cómo crear el Handler de BUSCAR
    @Bean
    public FindCourseQueryHandler findCourseQueryHandler(H2CourseRepository repository) {
        return new FindCourseQueryHandler(repository);
    }
}

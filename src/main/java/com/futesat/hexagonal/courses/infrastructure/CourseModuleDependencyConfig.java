package com.futesat.hexagonal.courses.infrastructure;

import com.futesat.hexagonal.courses.application.create.CreateCourseCommandHandler;
import com.futesat.hexagonal.courses.application.find.FindCourseQueryHandler;
import com.futesat.hexagonal.courses.infrastructure.persistence.jpa.JpaCourseRepository;
import com.futesat.hexagonal.shared.domain.bus.event.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseModuleDependencyConfig {

    // We teach Spring how to create the CREATE Handler
    // Enseñamos a Spring cómo crear el Handler de CREAR
    @Bean
    public CreateCourseCommandHandler createCourseCommandHandler(JpaCourseRepository repository, EventBus eventBus) {
        return new CreateCourseCommandHandler(repository, eventBus);
    }

    // We teach Spring how to create the SEARCH Handler
    // Enseñamos a Spring cómo crear el Handler de BUSCAR
    @Bean
    public FindCourseQueryHandler findCourseQueryHandler(JpaCourseRepository repository) {
        return new FindCourseQueryHandler(repository);
    }
}

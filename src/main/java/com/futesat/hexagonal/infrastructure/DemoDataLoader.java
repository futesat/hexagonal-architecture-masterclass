package com.futesat.hexagonal.infrastructure;

import com.futesat.hexagonal.courses.application.create.CreateCourseCommand;
import com.futesat.hexagonal.courses.application.create.CreateCourseCommandHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // No ejecutar durante los tests
public class DemoDataLoader implements CommandLineRunner {

    private final CreateCourseCommandHandler createCourseCommandHandler;

    public DemoDataLoader(CreateCourseCommandHandler createCourseCommandHandler) {
        this.createCourseCommandHandler = createCourseCommandHandler;
    }

    @Override
    public void run(String... args) {
        // Curso 1: Hexagonal
        createCourseCommandHandler.handle(new CreateCourseCommand(
                "1a9b456b-e85b-4b2a-a92c-d9a2c6d4838f",
                "Arquitectura Hexagonal Masterclass",
                "10 horas"));

        // Curso 2: DDD
        createCourseCommandHandler.handle(new CreateCourseCommand(
                "2b9b456b-e85b-4b2a-a92c-d9a2c6d4838f",
                "Domain-Driven Design Tactical Patterns",
                "15 horas"));

        System.out.println("🚀 [DemoDataLoader] Cursos de demostración cargados en la base de datos.");
    }
}

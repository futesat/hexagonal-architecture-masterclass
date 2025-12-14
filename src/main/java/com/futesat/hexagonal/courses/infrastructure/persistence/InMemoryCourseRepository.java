package com.futesat.hexagonal.courses.infrastructure.persistence;

import com.futesat.hexagonal.courses.domain.Course;
import com.futesat.hexagonal.courses.domain.CourseId;
import com.futesat.hexagonal.courses.domain.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryCourseRepository implements CourseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCourseRepository.class);
    private final Map<String, Course> courses = new HashMap<>();

    @Override
    public void save(Course course) {
        // We save using the primitive ID (Value Object -> Primitive)
        // Guardamos usando el ID primitives (Value Object -> Primitivo)
        courses.put(course.id().getValue(), course);
        LOGGER.info("DEBUG: Curso guardado en memoria: {}", course.name().getValue());
    }

    @Override
    public Optional<Course> search(CourseId id) {
        return Optional.ofNullable(courses.get(id.getValue()));
    }
}

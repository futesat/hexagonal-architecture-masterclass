package com.futesat.hexagonal.courses.domain;

import java.util.Optional;

// Puerto (Port) de salida.
// Define QUÉ necesitamos hacer con la BBDD, pero no CÓMO.
// La implementación (Adapter) irá en 'infrastructure'.
public interface CourseRepository {
    void save(Course course);

    Optional<Course> search(CourseId id);
}

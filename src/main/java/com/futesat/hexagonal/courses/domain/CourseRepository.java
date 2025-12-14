package com.futesat.hexagonal.courses.domain;

import java.util.Optional;

// Output Port.
// Puerto (Port) de salida.
// Defines WHAT we need to do with the DB, but not HOW.
// Define QUÉ necesitamos hacer con la BBDD, pero no CÓMO.
// The implementation (Adapter) will go in 'infrastructure'.
// La implementación (Adapter) irá en 'infrastructure'.
public interface CourseRepository {
    void save(Course course);

    Optional<Course> search(CourseId id);
}

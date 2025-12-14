package com.futesat.hexagonal.courses.infrastructure.persistence.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio "mágico" nativo de Spring Data.
// Solo es accesible desde la capa de infraestructura.
@Repository
public interface SpringDataCourseRepository extends JpaRepository<CourseEntity, String> {
}

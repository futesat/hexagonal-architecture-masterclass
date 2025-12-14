package com.futesat.hexagonal.courses.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Native "magic" Spring Data repository.
// Repositorio "mágico" nativo de Spring Data.
// Only accessible from the infrastructure layer.
// Solo es accesible desde la capa de infraestructura.
@Repository
public interface SpringDataCourseRepository extends JpaRepository<CourseEntity, String> {
}

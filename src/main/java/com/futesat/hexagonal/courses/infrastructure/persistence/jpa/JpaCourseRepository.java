package com.futesat.hexagonal.courses.infrastructure.persistence.jpa;

import com.futesat.hexagonal.courses.domain.Course;
import com.futesat.hexagonal.courses.domain.CourseId;
import com.futesat.hexagonal.courses.domain.CourseRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary // We mark this as primary so Spring chooses it if there is a conflict
// Marcamos este como el principal para que Spring lo elija si hay conflicto
// (although we will inject it manually)
// (aunque lo inyectaremos manualmente)
public class JpaCourseRepository implements CourseRepository {

    private final SpringDataCourseRepository jpaRepository;

    public JpaCourseRepository(SpringDataCourseRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Course course) {
        CourseEntity entity = new CourseEntity(
                course.id().getValue(),
                course.name().getValue(),
                course.duration());
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Course> search(CourseId id) {
        return jpaRepository.findById(id.getValue())
                .map(entity -> Course.from(
                        entity.getId(),
                        entity.getName(),
                        entity.getDuration()));
    }
}

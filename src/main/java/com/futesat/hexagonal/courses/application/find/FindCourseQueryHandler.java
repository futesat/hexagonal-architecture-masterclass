package com.futesat.hexagonal.courses.application.find;

import com.futesat.hexagonal.courses.domain.CourseId;
import com.futesat.hexagonal.courses.domain.CourseRepository;

public class FindCourseQueryHandler {

    private final CourseRepository repository;

    public FindCourseQueryHandler(CourseRepository repository) {
        this.repository = repository;
    }

    public CourseResponse handle(String id) {
        CourseId courseId = new CourseId(id); // We validate UUID format when instantiating
                                              // Validamos formato UUID al instanciar

        return repository.search(courseId)
                .map(CourseResponse::fromAggregate)
                .orElseThrow(() -> new CourseNotFound(id));
    }
}

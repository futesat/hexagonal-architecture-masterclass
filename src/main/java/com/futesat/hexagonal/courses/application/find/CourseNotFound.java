package com.futesat.hexagonal.courses.application.find;

public class CourseNotFound extends RuntimeException {
    public CourseNotFound(String id) {
        super("Course not found with id: " + id);
    }
}

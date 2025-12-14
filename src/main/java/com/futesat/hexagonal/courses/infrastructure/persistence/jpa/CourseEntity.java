package com.futesat.hexagonal.courses.infrastructure.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class CourseEntity {

    @Id
    private String id;
    private String name;
    private String duration;

    // Empty constructor required by JPA
    // Constructor vacío requerido por JPA
    public CourseEntity() {
    }

    public CourseEntity(String id, String name, String duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }
}

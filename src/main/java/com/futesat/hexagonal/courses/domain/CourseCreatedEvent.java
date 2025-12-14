package com.futesat.hexagonal.courses.domain;

import com.futesat.hexagonal.shared.domain.bus.event.DomainEvent;

public class CourseCreatedEvent extends DomainEvent {
    private final String name;
    private final String duration;

    public CourseCreatedEvent(String id, String name, String duration) {
        super(id);
        this.name = name;
        this.duration = duration;
    }

    @Override
    public String eventName() {
        return "course.created";
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }
}

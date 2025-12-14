package com.futesat.hexagonal.courses.domain;

import com.futesat.hexagonal.shared.domain.bus.event.DomainEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void should_return_course_created_event_when_course_is_created() {
        // GIVEN
        CourseId id = new CourseId("5a02e5b0-394c-4235-8656-78225586618e");
        CourseName name = new CourseName("Hexagonal Architecture");
        String duration = "5 hours";

        // WHEN
        Course course = Course.create(id, name, duration);
        List<DomainEvent> events = course.pullDomainEvents();

        // THEN
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof CourseCreatedEvent);
        CourseCreatedEvent event = (CourseCreatedEvent) events.get(0);
        assertEquals(id.getValue(), event.aggregateId());
        assertEquals(name.getValue(), event.getName());
        assertEquals(duration, event.getDuration());
    }

    @Test
    void should_not_publish_events_when_rehydrating_from_infrastructure() {
        // WHEN
        Course course = Course.from(
                "5a02e5b0-394c-4235-8656-78225586618e",
                "Legacy Code",
                "10 hours");
        List<DomainEvent> events = course.pullDomainEvents();

        // THEN
        assertTrue(events.isEmpty());
    }

    @Test
    void should_be_equals_by_id() {
        // GIVEN
        Course course1 = Course.from("5a02e5b0-394c-4235-8656-78225586618e", "Name 1", "Duration 1");
        Course course2 = Course.from("5a02e5b0-394c-4235-8656-78225586618e", "Name 2", "Duration 2");
        Course course3 = Course.from("11111111-1111-1111-1111-111111111111", "Name 1", "Duration 1");

        // THEN
        assertEquals(course1, course2); // Same ID
        assertNotEquals(course1, course3); // Different ID
        assertNotEquals(course1, null);
        assertNotEquals(course1, new Object());
        assertEquals(course1, course1);
        assertEquals(course1.hashCode(), course2.hashCode());
    }
}

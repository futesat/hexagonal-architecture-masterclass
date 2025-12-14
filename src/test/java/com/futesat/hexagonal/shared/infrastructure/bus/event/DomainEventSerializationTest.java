package com.futesat.hexagonal.shared.infrastructure.bus.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.futesat.hexagonal.courses.domain.CourseCreatedEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainEventSerializationTest {

    @Test
    void should_serialize_domain_event_to_json() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        CourseCreatedEvent event = new CourseCreatedEvent("id-123", "Intro to Hexagonal", "10 hours");

        // When
        String json = mapper.writeValueAsString(event);
        System.out.println("Serialized Event: " + json);

        // Then
        assertTrue(json.contains("\"aggregateId\":\"id-123\""));
        assertTrue(json.contains("\"name\":\"Intro to Hexagonal\""));
        assertTrue(json.contains("\"duration\":\"10 hours\""));
        assertTrue(json.contains("\"eventName\":\"course.created\""));
        assertTrue(json.contains("\"occurredOn\""));
        assertTrue(json.contains("\"eventId\""));

        System.out.println("Serialized Event: " + json);
    }
}

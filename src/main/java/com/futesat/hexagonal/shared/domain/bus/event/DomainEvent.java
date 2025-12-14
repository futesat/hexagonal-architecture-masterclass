package com.futesat.hexagonal.shared.domain.bus.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

// Base class for any Domain Event.
// Clase base para cualquier Evento de Dominio.
// An event is something that ALREADY HAPPENED (past tense). Immutable.
// Un evento es algo QUE YA PASÓ (nombre en pasado). Inmutable.
public abstract class DomainEvent implements Serializable {
    private final String aggregateId;
    private final String eventId;
    private final LocalDateTime occurredOn;

    public DomainEvent(String aggregateId) {
        this.aggregateId = aggregateId;
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String aggregateId() {
        return aggregateId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    public String getEventName() {
        return eventName();
    }

    // Each event must have a unique name (e.g., "course.created")
    // Cada evento debe tener un nombre único (ej: "course.created")
    public abstract String eventName();

}

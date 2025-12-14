package com.futesat.hexagonal.shared.domain;

import com.futesat.hexagonal.shared.domain.bus.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Base class for Aggregates that want an event system.
// Clase base para Agregados que quieran sistema de eventos.
public abstract class AggregateRoot {

    private List<DomainEvent> domainEvents = new ArrayList<>();

    // Method to return and clear events (pull & flush)
    // Método para devolver y limpiar eventos (pull & flush)
    public final List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return Collections.unmodifiableList(events);
    }

    // Protected method for entities to register what happened
    // Método protegido para que las entidades registren qué pasó
    protected final void record(DomainEvent event) {
        domainEvents.add(event);
    }
}

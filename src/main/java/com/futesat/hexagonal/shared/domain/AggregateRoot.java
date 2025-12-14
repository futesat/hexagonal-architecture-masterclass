package com.futesat.hexagonal.shared.domain;

import com.futesat.hexagonal.shared.domain.bus.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Clase base para Agregados que quieran sistema de eventos.
public abstract class AggregateRoot {

    private List<DomainEvent> domainEvents = new ArrayList<>();

    // Método para devolver y limpiar eventos (pull & flush)
    public final List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return Collections.unmodifiableList(events);
    }

    // Método protegido para que las entidades registren qué pasó
    protected final void record(DomainEvent event) {
        domainEvents.add(event);
    }
}

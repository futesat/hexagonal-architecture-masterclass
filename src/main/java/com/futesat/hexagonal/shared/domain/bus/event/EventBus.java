package com.futesat.hexagonal.shared.domain.bus.event;

import java.util.List;

// Puerto para publicar eventos.
public interface EventBus {
    void publish(List<DomainEvent> events);
}

package com.futesat.hexagonal.shared.infrastructure.bus.event.spring;

import com.futesat.hexagonal.shared.domain.bus.event.DomainEvent;
import com.futesat.hexagonal.shared.domain.bus.event.EventBus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringApplicationEventBus implements EventBus {

    private final ApplicationEventPublisher publisher;

    public SpringApplicationEventBus(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        // Publicamos cada evento individualmente en el sistema de Spring
        events.forEach(publisher::publishEvent);
    }
}

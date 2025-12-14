package com.futesat.hexagonal.notifications.infrastructure;

import com.futesat.hexagonal.courses.domain.CourseCreatedEvent;
import com.futesat.hexagonal.notifications.application.SendWelcomeEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.springframework.scheduling.annotation.Async;

@Component
public class WelcomeEmailSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeEmailSubscriber.class);
    private final SendWelcomeEmail sendWelcomeEmail;

    public WelcomeEmailSubscriber(SendWelcomeEmail sendWelcomeEmail) {
        this.sendWelcomeEmail = sendWelcomeEmail;
    }

    @Async
    @EventListener
    public void on(CourseCreatedEvent event) {
        // Adaptador: Convierte el evento en una llamada al caso de uso
        sendWelcomeEmail.send(event.getName());

        LOGGER.info("Subscriber consumió el evento para el curso: {}", event.getName());
    }
}

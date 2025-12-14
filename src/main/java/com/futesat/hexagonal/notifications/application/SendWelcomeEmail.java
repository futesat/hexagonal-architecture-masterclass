package com.futesat.hexagonal.notifications.application;

import com.futesat.hexagonal.notifications.domain.EmailSender;

public class SendWelcomeEmail {
    private final EmailSender emailSender;

    public SendWelcomeEmail(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void send(String courseName) {
        String subject = "Bienvenido al curso: " + courseName;
        String body = "¡Enhorabuena! Se ha creado el curso '" + courseName + "' exitosamente.";
        // Simulamos un destinatario fijo
        emailSender.send("admin@hexagonal.university", subject, body);
    }
}

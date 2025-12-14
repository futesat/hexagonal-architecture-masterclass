package com.futesat.hexagonal.notifications.infrastructure;

import com.futesat.hexagonal.notifications.domain.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FakeEmailSender implements EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(FakeEmailSender.class);

    @Override
    public void send(String recipient, String subject, String body) {
        try {
            Thread.sleep(2000); // Simulamos 2 segundos de latencia (SMTP lento)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        LOGGER.info("========================================");
        LOGGER.info("📧 MOCK EMAIL ENVIADO");
        LOGGER.info("   To: {}", recipient);
        LOGGER.info("   Subject: {}", subject);
        LOGGER.info("   Body: {}", body);
        LOGGER.info("========================================");
    }
}

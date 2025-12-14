package com.futesat.hexagonal.notifications.infrastructure;

import com.futesat.hexagonal.notifications.application.SendWelcomeEmail;
import com.futesat.hexagonal.notifications.domain.EmailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationsModuleDependencyConfig {

    @Bean
    public SendWelcomeEmail sendWelcomeEmail(EmailSender emailSender) {
        return new SendWelcomeEmail(emailSender);
    }
}

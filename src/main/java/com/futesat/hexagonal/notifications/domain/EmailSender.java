package com.futesat.hexagonal.notifications.domain;

public interface EmailSender {
    void send(String recipient, String subject, String body);
}

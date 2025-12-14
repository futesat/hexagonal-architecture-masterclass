package com.futesat.hexagonal.notifications.infrastructure;

import com.futesat.hexagonal.courses.domain.CourseCreatedEvent;
import com.futesat.hexagonal.notifications.domain.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.timeout;

@SpringBootTest
class WelcomeEmailIntegrationTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @MockBean
    private EmailSender emailSender;

    @Test
    void should_send_welcome_email_when_course_created_event_is_published() {
        // Given
        String courseId = "1a2b3c-course-id";
        String courseName = "Arquitectura Hexagonal en Java";
        String duration = "25 hours";

        CourseCreatedEvent event = new CourseCreatedEvent(courseId, courseName, duration);

        // When
        publisher.publishEvent(event);

        // Then
        // Allow time for async processing (+ artificial latency of 2000ms)
        verify(emailSender, timeout(3000)).send(
                anyString(),
                anyString(),
                // We check that the body contains the course name
                org.mockito.ArgumentMatchers.contains(courseName));
    }
}

package com.futesat.hexagonal.courses.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futesat.hexagonal.courses.application.create.CreateCourseCommand;
import com.futesat.hexagonal.courses.application.create.CreateCourseCommandHandler;
import com.futesat.hexagonal.courses.application.find.FindCourseQueryHandler;
import com.futesat.hexagonal.courses.infrastructure.api.CoursePostController.CourseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Probamos solo la capa Web (Controller) sin levantar toda la app
@WebMvcTest(CoursePostController.class)
public class CoursePostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Simulamos el Handler porque aquí solo probamos la capa HTTP -> Aplicación
    @MockBean
    private CreateCourseCommandHandler createCourseCommandHandler;

    @MockBean
    private FindCourseQueryHandler findCourseQueryHandler;

    @Test
    void should_return_created_when_posting_valid_course() throws Exception {
        CourseRequest request = new CourseRequest(
                "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                "Spring Boot Hexagonal",
                "5 hours");

        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Verificamos que el controlador llamó al caso de uso correctamente
        verify(createCourseCommandHandler).handle(any(CreateCourseCommand.class));
    }

    @Test
    void should_return_bad_request_when_posting_invalid_course() throws Exception {
        CourseRequest request = new CourseRequest(
                "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                "Hi", // Invalid: less than 5 chars
                "5 hours");

        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

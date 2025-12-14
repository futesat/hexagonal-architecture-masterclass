package com.futesat.hexagonal.courses.infrastructure.api;

import com.futesat.hexagonal.courses.application.create.CreateCourseCommand;
import com.futesat.hexagonal.courses.application.create.CreateCourseCommandHandler;
import com.futesat.hexagonal.courses.application.find.CourseResponse;
import com.futesat.hexagonal.courses.application.find.FindCourseQueryHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
public class CoursePostController {

    private final CreateCourseCommandHandler createCourseCommandHandler;
    private final FindCourseQueryHandler findCourseQueryHandler;

    public CoursePostController(
            CreateCourseCommandHandler createCourseCommandHandler,
            FindCourseQueryHandler findCourseQueryHandler) {
        this.createCourseCommandHandler = createCourseCommandHandler;
        this.findCourseQueryHandler = findCourseQueryHandler;
    }

    @Operation(summary = "Crear un nuevo curso", description = "Registra un curso en el sistema y notifica el evento de creación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. nombre corto)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/courses")
    public ResponseEntity<String> create(@Valid @RequestBody CourseRequest request) {
        createCourseCommandHandler.handle(
                new CreateCourseCommand(
                        request.id(),
                        request.name(),
                        request.duration()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar curso por ID", description = "Recupera la información detallada de un curso existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "El curso con el ID especificado no existe")
    })
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponse> get(@PathVariable String id) {
        CourseResponse response = findCourseQueryHandler.handle(id);
        return ResponseEntity.ok(response);
    }

    // DTO interno para el Request Body
    public record CourseRequest(
            @NotBlank(message = "El ID es obligatorio") String id,

            @NotBlank(message = "El nombre es obligatorio") @Size(min = 5, message = "El nombre debe tener al menos 5 caracteres") String name,

            @NotBlank(message = "La duración es obligatoria") String duration) implements Serializable {
    }
}

package com.futesat.hexagonal.courses.domain;

import com.futesat.hexagonal.shared.domain.Identifier;

// Ahora CourseId es mucho más simple y hereda toda la potencia de validación de UUID
public class CourseId extends Identifier {

    public CourseId(String value) {
        super(value);
    }

    // Aquí podríamos añadir métodos específicos si fuera necesario
    // pero la validación base ya está hecha en el padre.
}

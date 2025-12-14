package com.futesat.hexagonal.courses.domain;

import com.futesat.hexagonal.shared.domain.Identifier;

// Now CourseId is much simpler and inherits all UUID validation power
// Ahora CourseId es mucho más simple y hereda toda la potencia de validación de UUID
public class CourseId extends Identifier {

    public CourseId(String value) {
        super(value);
    }

    // We could add specific methods here if necessary
    // Aquí podríamos añadir métodos específicos si fuera necesario
    // but base validation is already done in the parent.
    // pero la validación base ya está hecha en el padre.
}

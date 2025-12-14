package com.futesat.hexagonal.shared.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

// Base class for Identifiers (Shared Kernel)
// Clase base para Identificadores (Shared Kernel)
// This avoids repeating UUID and null validations in every ID we create (CourseId, StudentId, UserId...)
// Esto evita repetir validaciones de UUID y nulls en cada ID que creemos (CourseId, StudentId, UserId...)
public abstract class Identifier implements Serializable {
    private final String value;

    protected Identifier(String value) {
        ensureValidUuid(value);
        this.value = value;
    }

    // Private method to ensure integrity
    // Método privado para asegurar integridad
    private void ensureValidUuid(String value) {
        try {
            UUID.fromString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UUID string: " + value);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Identifier that = (Identifier) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

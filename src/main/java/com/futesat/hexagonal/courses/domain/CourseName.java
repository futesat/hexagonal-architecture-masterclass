package com.futesat.hexagonal.courses.domain;

import java.util.Objects;

// Value Object for Name
// Value Object para el Nombre
public class CourseName {
    private final String value;

    public CourseName(String value) {
        if (value == null || value.length() < 5) {
            throw new IllegalArgumentException("The course name must be at least 5 characters long");
        }
        this.value = value;
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
        CourseName that = (CourseName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

# Automatic detection of Java 21 on macOS
# Detección automática de Java 21 en macOS
OS := $(shell uname)
ifeq ($(OS),Darwin)
	JAVA_HOME := $(shell /usr/libexec/java_home -v 21)
	export JAVA_HOME
endif

.PHONY: all build test run clean docker-build docker-run docker-run-postgres

# Default target
# Objetivo por defecto
all: build

# Compile and package
# Compilar y empaquetar
build:
	./mvnw clean package -DskipTests

# Run ALL tests (Unit, Integration, Architecture)
# Ejecutar TODOS los tests (Unitarios, Integración, Arquitectura)
test:
	./mvnw test

# Start application locally
# Arrancar la aplicación localmente
run:
	./mvnw spring-boot:run

# Clean target directory
# Limpiar directorio target
clean:
	./mvnw clean

# Build Docker image
# Construir imagen Docker
docker-build:
	docker build -t hexagonal-architecture-masterclass .

# Run Docker container
# Ejecutar contenedor Docker
docker-run:
	docker run -p 8080:8080 hexagonal-architecture-masterclass

# Run application with PostgreSQL (Docker Compose)
# Ejecutar aplicación con PostgreSQL (Docker Compose)
docker-run-postgres:
	docker-compose up --build

# Start full environment (App + Postgres)
# Levantar entorno completo (App + Postgres)
compose-up:
	docker-compose up --build

# Destroy environment
# Destruir entorno
compose-down:
	docker-compose down

# Health Check
# Verificar salud (Health Check)
health:
	curl http://localhost:8080/actuator/health

# Check metrics
# Verificar métricas
metrics:
	curl http://localhost:8080/actuator/metrics

# Generate and open coverage report (JaCoCo)
# Generar y abrir reporte de cobertura (JaCoCo)
coverage: test
	open target/site/jacoco/index.html

# Help
# Ayuda
help:
	@echo "Comandos disponibles:"
	@echo "  make build               - Compilar el proyecto (sin tests)"
	@echo "  make test                - Ejecutar todos los tests"
	@echo "  make run                 - Arrancar la aplicación"
	@echo "  make docker-build        - Construir imagen Docker"
	@echo "  make docker-run          - Correr contenedor Docker (H2 Memoria)"
	@echo "  make docker-run-postgres - Correr aplicación + PostgreSQL (Docker Compose)"
	@echo "  make health              - Comprobar estado (requiere app corriendo)"

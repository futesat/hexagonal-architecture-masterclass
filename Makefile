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
	@echo "Available commands:"
	@echo "  make build               - Compile the project (skipping tests)"
	@echo "  make clean               - Clean target directory"
	@echo "  make test                - Run all tests"
	@echo "  make coverage            - Generate and open coverage report (JaCoCo)"
	@echo "  make run                 - Run the application"
	@echo "  make health              - Check health status (requires running app)"
	@echo "  make metrics             - Check metrics (requires running app)"
	@echo "  make docker-build        - Build Docker image"
	@echo "  make docker-run          - Run Docker container (H2 Memory)"
	@echo "  make docker-run-postgres - Run application + PostgreSQL (Docker Compose)"
	@echo "  make compose-up          - Start full environment (App + Postgres)"
	@echo "  make compose-down        - Destroy Docker Compose environment"

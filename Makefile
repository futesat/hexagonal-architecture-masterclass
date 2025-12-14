# Detección automática de Java 21 en macOS
OS := $(shell uname)
ifeq ($(OS),Darwin)
	JAVA_HOME := $(shell /usr/libexec/java_home -v 21)
	export JAVA_HOME
endif

.PHONY: all build test run clean docker-build docker-run docker-run-postgres

# Objetivo por defecto
all: build

# Compilar y empaquetar
build:
	./mvnw clean package -DskipTests

# Ejecutar TODOS los tests (Unitarios, Integración, Arquitectura)
test:
	./mvnw test

# Arrancar la aplicación localmente
run:
	./mvnw spring-boot:run

# Limpiar directorio target
clean:
	./mvnw clean

# Construir imagen Docker
docker-build:
	docker build -t hexagonal-architecture-masterclass .

# Ejecutar contenedor Docker
docker-run:
	docker run -p 8080:8080 hexagonal-architecture-masterclass

# Ejecutar aplicación con PostgreSQL (Docker Compose)
docker-run-postgres:
	docker-compose up --build

# Levantar entorno completo (App + Postgres)
compose-up:
	docker-compose up --build

# Destruir entorno
compose-down:
	docker-compose down

# Verificar salud (Health Check)
health:
	curl http://localhost:8080/actuator/health

# Verificar métricas
metrics:
	curl http://localhost:8080/actuator/metrics

# Generar y abrir reporte de cobertura (JaCoCo)
coverage: test
	open target/site/jacoco/index.html

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

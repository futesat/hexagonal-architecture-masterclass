.PHONY: all build test run clean docker-build docker-run

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
	@echo "  make build         - Compilar el proyecto (sin tests)"
	@echo "  make test          - Ejecutar todos los tests"
	@echo "  make run           - Arrancar la aplicación"
	@echo "  make docker-build  - Construir imagen Docker"
	@echo "  make docker-run    - Correr contenedor Docker"
	@echo "  make health        - Comprobar estado (requiere app corriendo)"

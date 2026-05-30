# stockflow_api

API REST backend para la gestión de inventario multi-almacén, productos, clientes, pedidos y movimientos de stock.

El objetivo de este proyecto es construir una API backend con enfoque profesional, preparada para escenarios reales de empresa: persistencia en PostgreSQL, migraciones con Flyway, seguridad JWT, auditoría automática, control de concurrencia y documentación OpenAPI.

## Objetivo del proyecto

`stockflow_api` nace como un proyecto de portfolio técnico orientado a demostrar capacidades reales de desarrollo backend Java en entornos empresariales.

El sistema modela un dominio de inventario y pedidos donde varios usuarios pueden operar sobre productos, almacenes y stock de forma concurrente, evitando inconsistencias como el overselling.

## Stack técnico

* Java 25
* Spring Boot 4
* Spring Web MVC
* Spring Data JPA
* Spring Security
* OAuth2 Resource Server / JWT
* PostgreSQL 17
* Flyway
* Hibernate
* MapStruct
* Lombok
* JSpecify
* OpenAPI 3 / Swagger UI
* Docker Compose
* JUnit 5
* Mockito
* AssertJ
* Testcontainers

## Funcionalidades previstas

* Gestión de productos
* Gestión de almacenes
* Gestión de stock multi-almacén
* Movimientos de stock
* Gestión de clientes
* Gestión de pedidos
* Control de usuarios y roles
* Autenticación mediante JWT
* Auditoría automática de entidades
* Control de concurrencia
* Validaciones de negocio
* API documentada con OpenAPI 3
* Tests unitarios e integración con Testcontainers

## Estado actual

### Completado

* Proyecto Spring Boot inicializado
* Configuración Maven
* Configuración Docker Compose con PostgreSQL
* Configuración por perfiles: dev, test y prod
* Migración inicial con Flyway
* Seguridad base stateless
* Swagger UI accesible
* Actuator configurado
* JPA Auditing global
* AuditorAware basado en Spring Security
* Entidad base auditable
* Entidad Product con auditoría y bloqueo optimista

### En desarrollo

* ProductRepository
* DTOs de Product
* ProductMapper con MapStruct
* ProductService
* ProductController
* Gestión de errores con RFC 7807 Problem Details
* Tests unitarios
* Tests de integración con Testcontainers

## Arquitectura

El proyecto sigue una estructura por dominio funcional.

```text
src/main/java/com/franciscomolina/stockflow_api/
├── common/
├── config/
└── product/
    ├── controller/
    ├── domain/
    ├── dto/
    ├── mapper/
    ├── repository/
    └── service/
```

Cada módulo de dominio agrupa sus propias responsabilidades, evitando una estructura técnica plana y facilitando el crecimiento del sistema.

## Auditoría automática

Las entidades principales heredan de una clase base auditable que permite registrar automáticamente:

* Fecha de creación
* Usuario creador
* Fecha de última modificación
* Usuario modificador

La auditoría se integra con Spring Security mediante `AuditorAware`.

Cuando existe un usuario autenticado, se registra su identificador.
Cuando no existe contexto de seguridad, se utiliza el auditor técnico `system`.

Esto permite mantener trazabilidad tanto en peticiones HTTP autenticadas como en procesos internos o batch.

## Control de concurrencia

La entidad `Product` incorpora bloqueo optimista mediante `@Version`.

Este mecanismo permite detectar modificaciones concurrentes sobre una misma entidad y evitar sobrescrituras silenciosas.

Más adelante, el control de stock incorporará una estrategia específica para evitar overselling en escenarios de alta concurrencia.

## Base de datos

El proyecto utiliza PostgreSQL como base de datos principal.

En desarrollo se levanta mediante Docker Compose.

```bash
docker compose up -d
```

La aplicación valida el esquema mediante Hibernate y Flyway se encarga de aplicar las migraciones.

## Ejecución local

### Requisitos

* Java 25
* Maven 3.9+
* Docker
* Docker Compose

### Levantar base de datos

```bash
docker compose up -d
```

### Arrancar aplicación

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw spring-boot:run
```

## Swagger UI

Una vez arrancada la aplicación, la documentación de la API estará disponible en:

```text
http://localhost:8080/swagger-ui.html
```

## Actuator

Endpoints básicos de salud:

```text
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
```

## Testing

El proyecto está preparado para incorporar:

* Tests unitarios con JUnit 5, Mockito y AssertJ
* Tests de integración con Testcontainers
* Tests de concurrencia para validar la consistencia del stock

Ejecutar tests:

```bash
./mvnw test
```

En Windows PowerShell:

```powershell
.\mvnw test
```

## Decisiones técnicas destacadas

* Uso de PostgreSQL como base de datos relacional principal
* Migraciones versionadas con Flyway
* Validación de esquema con Hibernate
* Separación de DTOs y entidades
* MapStruct para mapeos entre capas
* Records de Java para DTOs
* Auditoría automática con Spring Data JPA
* Seguridad stateless preparada para JWT
* `BigDecimal` para importes monetarios
* Bloqueo optimista con `@Version`
* Preparación para tests de integración reales con Testcontainers

## Valor técnico del proyecto

Este proyecto no busca ser un CRUD básico, sino una API backend diseñada con criterios de producción:

* Arquitectura mantenible
* Persistencia robusta
* Trazabilidad
* Seguridad
* Control de concurrencia
* Documentación útil
* Testing automatizado
* Preparación para despliegue

## Contribuciones

Las contribuciones son bienvenidas.

Si encuentras un problema, una mejora o quieres proponer una nueva funcionalidad, puedes abrir un Issue o enviar un Pull Request.

Antes de contribuir, asegúrate de:

- Mantener la coherencia con la arquitectura existente.
- Añadir o actualizar las pruebas correspondientes.
- Documentar los cambios relevantes.
- Seguir las convenciones de código del proyecto.

## Licencia

Este proyecto se distribuye bajo la licencia MIT.

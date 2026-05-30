# StockFlow API

Backend REST API diseñada para gestionar inventario, almacenes, stock y pedidos en entornos empresariales.

StockFlow API está construida siguiendo prácticas modernas de desarrollo backend con Java y Spring Boot, incorporando auditoría automática, control de concurrencia, documentación OpenAPI, seguridad JWT y una arquitectura preparada para producción.

## ¿Por qué este proyecto?

La mayoría de ejemplos de inventario terminan siendo simples CRUDs.

StockFlow API busca resolver problemas reales que aparecen en aplicaciones empresariales:

* Gestión de stock en múltiples almacenes.
* Trazabilidad completa de cambios.
* Control de concurrencia para evitar inconsistencias.
* Seguridad basada en JWT.
* Migraciones versionadas de base de datos.
* Testing automatizado.
* Arquitectura escalable y mantenible.

## Características principales

* Gestión de productos.
* Gestión de almacenes.
* Gestión de stock multi-almacén.
* Gestión de pedidos.
* Gestión de clientes.
* Auditoría automática de entidades.
* Control de concurrencia mediante Optimistic Locking.
* Documentación OpenAPI / Swagger.
* Validación de datos.
* Arquitectura orientada al dominio.
* Integración con PostgreSQL.
* Tests unitarios e integración.

## Stack tecnológico

| Categoría       | Tecnología                  |
| --------------- | --------------------------- |
| Lenguaje        | Java 25                     |
| Framework       | Spring Boot 4               |
| Persistencia    | Spring Data JPA + Hibernate |
| Base de datos   | PostgreSQL 17               |
| Migraciones     | Flyway                      |
| Seguridad       | Spring Security + JWT       |
| Mapeo DTOs      | MapStruct                   |
| Documentación   | OpenAPI 3                   |
| Testing         | JUnit 5, Mockito, AssertJ   |
| Integración     | Testcontainers              |
| Infraestructura | Docker Compose              |

## Arquitectura

El proyecto sigue una estructura modular basada en dominio:

```text
product/
warehouse/
stock/
customer/
order/
security/
common/
config/
```

Cada módulo encapsula su propia lógica de negocio, persistencia, servicios y API REST.

## Aspectos técnicos destacados

### Auditoría automática

Todas las entidades de negocio heredan de una entidad base auditable que registra automáticamente:

* Fecha de creación.
* Usuario creador.
* Fecha de modificación.
* Usuario modificador.

### Control de concurrencia

Las entidades utilizan Optimistic Locking mediante `@Version` para detectar modificaciones concurrentes.

El módulo de stock incorporará mecanismos adicionales para prevenir overselling en escenarios de alta concurrencia.

### API First

La API se documenta mediante OpenAPI 3 y Swagger UI para facilitar su consumo e integración.

## Ejecución local

### Levantar PostgreSQL

```bash
docker compose up -d
```

### Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

### Ejecutar tests

```bash
./mvnw test
```

## Estado del proyecto

Proyecto en desarrollo activo.

Actualmente se encuentra implementada la infraestructura base, auditoría global y el primer agregado del dominio (`Product`).

Las siguientes iteraciones incorporarán gestión completa de stock, pedidos, seguridad JWT y pruebas de concurrencia.

## Licencia

MIT License.

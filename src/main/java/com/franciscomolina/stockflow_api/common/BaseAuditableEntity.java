package com.franciscomolina.stockflow_api.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Clase base para entidades que necesitan trazabilidad de creación y
 * modificación.
 *
 * <p>Se define como {@link MappedSuperclass} porque no representa una tabla
 * propia, sino columnas comunes que deben heredarse en las tablas reales del
 * dominio. Esto mantiene el modelo limpio y evita duplicar auditoría en cada
 * entidad.</p>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditableEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false, length = 100)
    private String updatedBy;

    /**
     * Devuelve la fecha de creación persistida por JPA.
     *
     * <p>Puede ser nula antes del primer {@code persist}, por eso el contrato
     * público lo expresa de forma explícita con JSpecify.</p>
     *
     * @return fecha de creación de la entidad
     */
    public @Nullable OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Devuelve el auditor que creó la entidad.
     *
     * <p>Puede ser nulo mientras la entidad todavía no haya sido persistida.</p>
     *
     * @return usuario creador de la entidad
     */
    public @Nullable String getCreatedBy() {
        return createdBy;
    }

    /**
     * Devuelve la última fecha de modificación persistida por JPA.
     *
     * <p>Puede ser nula antes del primer ciclo de persistencia gestionado por
     * Hibernate.</p>
     *
     * @return fecha de última modificación
     */
    public @Nullable OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Devuelve el auditor que realizó la última modificación.
     *
     * <p>Puede ser nulo antes de que Spring Data JPA haya aplicado la auditoría
     * sobre la entidad.</p>
     *
     * @return usuario responsable de la última modificación
     */
    public @Nullable String getUpdatedBy() {
        return updatedBy;
    }
}
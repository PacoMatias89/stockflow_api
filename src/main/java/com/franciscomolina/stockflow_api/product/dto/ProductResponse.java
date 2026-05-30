package com.franciscomolina.stockflow_api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;

/**
 * Representación pública de un producto devuelta por la API.
 *
 * <p>Incluye datos funcionales, versión de concurrencia y auditoría para que
 * los clientes puedan trabajar con trazabilidad y detectar cambios concurrentes.</p>
 */
@Schema(description = "Producto devuelto por la API.")
public record ProductResponse(

        @Schema(description = "Identificador técnico del producto.", example = "1")
        Long id,

        @Schema(description = "Identificador funcional único del producto.", example = "LAPTOP-DELL-15")
        String sku,

        @Schema(description = "Nombre comercial del producto.", example = "Dell Latitude 5550")
        String name,

        @Schema(description = "Descripción del producto.", example = "Portátil corporativo de 15 pulgadas.")
        @Nullable
        String description,

        @Schema(description = "Precio unitario del producto.", example = "1299.99")
        BigDecimal price,

        @Schema(description = "Categoría funcional del producto.", example = "Electronics")
        String category,

        @Schema(description = "Versión usada para control de concurrencia.", example = "0")
        Long version,

        @Schema(description = "Fecha de creación del registro.")
        @Nullable
        OffsetDateTime createdAt,

        @Schema(description = "Usuario que creó el registro.", example = "system")
        @Nullable
        String createdBy,

        @Schema(description = "Fecha de última modificación del registro.")
        @Nullable
        OffsetDateTime updatedAt,

        @Schema(description = "Usuario que realizó la última modificación.", example = "system")
        @Nullable
        String updatedBy
) {
}
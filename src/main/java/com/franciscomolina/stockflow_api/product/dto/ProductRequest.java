package com.franciscomolina.stockflow_api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

/**
 * Datos de entrada necesarios para crear o actualizar un producto.
 *
 * <p>Este record actúa como frontera pública de la API y concentra las
 * validaciones que protegen al dominio frente a datos incompletos o inválidos.</p>
 */
@Schema(description = "Datos necesarios para crear o actualizar un producto.")
public record ProductRequest(

        @Schema(
                description = "Identificador funcional único del producto.",
                example = "LAPTOP-DELL-15"
        )
        @NotBlank(message = "El SKU es obligatorio.")
        @Size(max = 100, message = "El SKU no puede superar los 100 caracteres.")
        String sku,

        @Schema(
                description = "Nombre comercial del producto.",
                example = "Dell Latitude 5550"
        )
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 255, message = "El nombre no puede superar los 255 caracteres.")
        String name,

        @Schema(
                description = "Descripción opcional del producto.",
                example = "Portátil corporativo de 15 pulgadas."
        )
        @Nullable
        @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres.")
        String description,

        @Schema(
                description = "Precio unitario del producto.",
                example = "1299.99"
        )
        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero.")
        @Digits(integer = 10, fraction = 2, message = "El precio debe tener como máximo 10 enteros y 2 decimales.")
        BigDecimal price,

        @Schema(
                description = "Categoría funcional del producto.",
                example = "Electronics"
        )
        @NotBlank(message = "La categoría es obligatoria.")
        @Size(max = 100, message = "La categoría no puede superar los 100 caracteres.")
        String category
) {
}
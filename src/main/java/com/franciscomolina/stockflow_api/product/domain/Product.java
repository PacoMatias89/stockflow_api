package com.franciscomolina.stockflow_api.product.domain;

import com.franciscomolina.stockflow_api.common.BaseAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

/**
 * Representa un producto comercializable dentro del sistema.
 *
 * <p>La entidad contiene únicamente información maestra del producto.
 * El stock físico se gestionará mediante la entidad Stock para permitir
 * escenarios multi-almacén y control de concurrencia independiente.</p>
 *
 * <p>La separación entre Product y Stock evita inconsistencias cuando
 * un mismo producto existe en múltiples almacenes.</p>
 */
@Getter
@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_sku", columnList = "sku"),
                @Index(name = "idx_products_category", columnList = "category")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;

    @Setter
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Setter
    @Column(name = "description", length = 2000)
    private String description;

    @Setter
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Setter
    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public Product(
            String sku,
            String name,
            @Nullable String description,
            BigDecimal price,
            String category
    ) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
}
package com.franciscomolina.stockflow_api.product.repository;

import com.franciscomolina.stockflow_api.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio encargado del acceso a datos de productos.
 *
 * <p>Centraliza las consultas específicas del agregado Product y evita
 * que la lógica de persistencia se disperse por las capas superiores.</p>
 */
@Repository
public interface PoductRepository extends JpaRepository<Product, Long> {
    /**
     * Comprueba si existe un producto con el SKU indicado.
     *
     * <p>Se utiliza principalmente para garantizar la unicidad funcional
     * antes de crear nuevos productos.</p>
     *
     * @param sku identificador funcional del producto
     * @return true si existe, false en caso contrario
     */

    boolean existsBySku(String sku);

    /**
     * Recupera un producto a partir de su SKU.
     *
     * @param sku identificador funcional del producto
     * @return producto encontrado si existe
     */

    Optional<Product> findBySku(String sku);

}

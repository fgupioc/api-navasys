package com.fgupioc.api_novasys.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 07/08/2026
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "inventario_stock",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inventario_stock_producto_almacen",
                        columnNames = {"product_id", "warehouse_id"}
                )
        }
)
@ToString(exclude = {"producto", "almacen"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InventarioStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Producto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Producto producto;

    /**
     * Almacén
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Almacen almacen;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal initialQuantity;
    /**
     * Stock actual
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    /**
     * Costo promedio actual del producto.
     */
    @Column(name = "average_cost", precision = 12, scale = 2)
    private BigDecimal averageCost;

    /**
     * Último costo de compra.
     */
    @Column(name = "last_cost", precision = 12, scale = 2)
    private BigDecimal lastCost;

    /**
     * Estado del registro.
     */
    @Column(name = "cod_status", nullable = false)
    private String codStatus;
}

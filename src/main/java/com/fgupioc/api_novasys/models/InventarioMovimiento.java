package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.helpers.CONST;
import com.fgupioc.api_novasys.models.enums.InventarioMovimientoTipo;
import com.fgupioc.api_novasys.models.enums.InventarioReferenciaTipo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventario_movimiento")
public class InventarioMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Almacen almacen;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private InventarioMovimientoTipo tipoMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false)
    private InventarioReferenciaTipo tipoReferencia;

    /**
     * ID de la operación:
     * compra, venta, transferencia, ajuste...
     */
    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    /**
     * Cantidad del movimiento.
     * <p>
     * Ejemplo:
     * Compra = +20
     * Venta  = -5
     */
    @Column(nullable = false)
    private BigDecimal quantity;

    /**
     * Stock existente antes del movimiento.
     */
    @Column(name = "stock_before", nullable = false)
    private BigDecimal stockBefore;

    /**
     * Stock resultante después del movimiento.
     */
    @Column(name = "stock_after", nullable = false)
    private BigDecimal stockAfter;

    /**
     * Costo unitario del movimiento.
     */
    @Column(name = "unit_cost", precision = 12, scale = 2)
    private BigDecimal unitCost;

    /**
     * Precio de venta utilizado en la operación,
     * si corresponde.
     */
    @Column(name = "sale_price", precision = 12, scale = 2)
    private BigDecimal salePrice;

    private String codStatus;

    @PrePersist
    public void prePersist() {
        this.codStatus = CONST.STATUS_ACTIVE;
    }
}
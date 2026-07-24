package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.models.enums.InventarioMovimientoTipo;
import com.fgupioc.api_novasys.models.enums.InventarioReferenciaTipo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
@SQLDelete(sql = "UPDATE inventario_movimiento SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class InventarioMovimiento extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Producto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Producto product;

    /**
     * Almacén
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Almacen warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private InventarioMovimientoTipo movementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", nullable = false)
    private InventarioReferenciaTipo referenceType;

    /**
     * Id de la compra, venta, transferencia...
     */
    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    /**
     * Positivo = Entrada
     * Negativo = Salida
     */
    private Double quantity;

    /**
     * Costo unitario del movimiento
     */
    private Double unitCost;

}
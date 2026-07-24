package com.fgupioc.api_novasys.models;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compra_detaller")
@SQLDelete(sql = "UPDATE compra_detaller SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class CompraDetalle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cabecera de la compra
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Compra compra;

    /**
     * Producto comprado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Producto producto;

    /**
     * Cantidad comprada
     */
    private Double quantity;

    /**
     * Costo unitario
     */
    @Column(name = "unit_cost")
    private Double unitCost;

    /**
     * Descuento aplicado
     */
    private Double discount;

    /**
     * Subtotal
     */
    private Double subtotal;

}
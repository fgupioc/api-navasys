package com.fgupioc.api_novasys.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

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
@Table(name = "compra_detalle")
public class CompraDetalle {

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
    private BigDecimal quantity;

    /**
     * Costo unitario
     */
    @Column(name = "unit_cost")
    private BigDecimal unitCost;
    @Column(name = "sale_price")
    private BigDecimal salePrice;

    /**
     * Descuento aplicado
     */
    private BigDecimal discount;

    /**
     * Subtotal
     */
    private BigDecimal subtotal;

}
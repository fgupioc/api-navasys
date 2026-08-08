package com.fgupioc.api_novasys.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "venta_detalle")
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Cabecera de la compra
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Venta venta;
    /*
     * Producto comprado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Producto producto;
    private String productName;
    /**
     * Cantidad comprada
     */
    private BigDecimal quantity;
    /**
     * Costo unitario
     */
    private BigDecimal unitPrice;
    /**
     * Descuento aplicado
     */
    private BigDecimal discount;
    /**
     * Subtotal
     */
    private BigDecimal subtotal;
}
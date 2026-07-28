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
 * @since 28/07/2026
 */
@Entity
@Table(name = "venta_pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Venta sale;

    private String method;          // EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA

    /** Monto que realmente se aplica a la venta (nunca incluye el vuelto) */
    private BigDecimal amount;


    /** Nº de operación de Yape/Plin, voucher de tarjeta, etc. */
    private String reference;
}
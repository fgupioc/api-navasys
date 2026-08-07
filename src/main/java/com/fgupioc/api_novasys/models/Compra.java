package com.fgupioc.api_novasys.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "compras")
@SQLDelete(sql = "UPDATE compras SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Compra extends BaseEntity {
    public static String STATUS_BORRADOR = "1";
    public static String STATUS_PENDIENTE = "2";
    public static String STATUS_RECIBIDA = "3";
    public static String STATUS_PARCIAL = "4";
    public static String STATUS_COMPLETADA = "5";
    public static String STATUS_ANULADA = "6";
    public static String STATUS_DEVUELTA = "7";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;
    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Almacen almacen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "document_type", nullable = false, length = 5)
    private String documentType;

    @Column(name = "document_series", length = 10)
    private String documentSeries;

    @Column(name = "document_number", length = 20)
    private String documentNumber;

    @Column(name = "payment_type", length = 20)
    private String paymentType;

    @Column(length = 5)
    private String currency;

    @Column(length = 300)
    private String observation;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(precision = 12, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    private BigDecimal taxApplied = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "cod_status")
    private String codStatus;

    @Column(name = "created_by")
    private Long createdBy;

    @PrePersist
    public void prePersist() {
        this.codStatus = STATUS_BORRADOR;
        this.uuid = UUID.randomUUID();
    }

}
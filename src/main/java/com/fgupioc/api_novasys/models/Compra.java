package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Almacen warehouse;

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

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "cod_status")
    private String codStatus;

    @Column(name = "created_by")
    private Long createdBy;

    @PrePersist
    public void prePersist() {
        codStatus = CONST.STATUS_ACTIVE;
    }

    /*
    public static PurchaseRespDTO toRespDTO(Purchase purchase) {
        return new PurchaseRespDTO(
                purchase.getId(),
                purchase.getCode(),
                purchase.getSupplier() != null ? purchase.getSupplier().getId() : null,
                purchase.getWarehouse() != null ? purchase.getWarehouse().getId() : null,
                purchase.getPurchaseDate(),
                purchase.getDocumentType(),
                purchase.getDocumentSeries(),
                purchase.getDocumentNumber(),
                purchase.getPaymentType(),
                purchase.getCurrency(),
                purchase.getObservation(),
                purchase.getSubtotal(),
                purchase.getDiscount(),
                purchase.getTax(),
                purchase.getTotal(),
                purchase.getCodStatus()
        );
    }
    */
}
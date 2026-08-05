package com.fgupioc.api_novasys.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 26/07/2026
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "ventas")
@SQLDelete(sql = "UPDATE ventas SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Builder
public class Venta extends BaseEntity {
    public static String STATUS_COMPLETADA = "1"; // La venta fue pagada correctamente.
    public static String STATUS_ANULADA = "2"; // La venta fue anulada.
    public static String STATUS_PENDIENTE = "3"; // La venta está pendiente de pago.
    public static String STATUS_DEVUELTA = "4"; // La venta fue devuelta.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    @Column(unique = true, nullable = false, length = 20)
    private String code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Cliente customer;
    private String documentType;
    private String documentSeries;
    private String documentNumber;
    private String paymentMethod;
    private LocalDate saleDate;
    private String codStatus;
    private String currency;
    @Column(length = 300)
    private String observation;
    private BigDecimal subtotal;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal taxApplied = BigDecimal.ZERO;
    private BigDecimal total;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caja_sesion_id")
    private CajaSesion cajaSesion;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaPago> payments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }

    public void addPayment(VentaPago p) {
        payments.add(p);
        p.setSale(this);
    }
}

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
 * @since 09/08/2026
 */
@Entity
@Table(name = "transferencia_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "transferencia_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_transferencia_detalle_transferencia"
            )
    )
    private Transferencia transferencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_transferencia_detalle_producto"
            )
    )
    private Producto product;

    @Column(
            name = "quantity",
            nullable = false,
            precision = 12,
            scale = 3
    )
    private BigDecimal quantity;

    @Column(
            name = "quantity_received",
            precision = 12,
            scale = 3
    )
    private BigDecimal quantityReceived;
}

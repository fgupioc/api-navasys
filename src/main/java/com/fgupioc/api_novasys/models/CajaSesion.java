package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.models.enums.EstadoSesionCaja;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 02/08/2026
 */
@Entity
@Table(name = "caja_sesion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SQLDelete(sql = "UPDATE caja_sesion SET deleted_at = NOW(6) WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class CajaSesion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cash_register_id")
    private Caja caja;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal openingAmount;
    @Column(precision = 18, scale = 2)
    private BigDecimal closingAmount;
    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSesionCaja status;
    @Column(length = 501)
    private String notes;
}

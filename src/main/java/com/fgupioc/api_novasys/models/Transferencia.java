package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.models.enums.EstadoTransferencia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 09/08/2026
 */

@Entity
@Table(
        name = "transferencias",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_transferencia_uuid", columnNames = "uuid"),
                @UniqueConstraint(name = "uk_transferencia_code", columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE transferencias SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Transferencia extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "source_warehouse_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transferencia_origen")
    )
    private Almacen sourceWarehouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "destination_warehouse_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transferencia_destino")
    )
    private Almacen destinationWarehouse;

    @Column(name = "code", nullable = false, unique = true, length = 30)
    private String code;

    @Column(name = "fecha_transferencia", nullable = false)
    private LocalDateTime fechaTransferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "cod_status", nullable = false, length = 20)
    private EstadoTransferencia codStatus;

    @Column(name = "observation", length = 500)
    private String observation;

    @OneToMany(
            mappedBy = "transferencia",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<TransferenciaDetalle> productos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}

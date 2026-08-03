package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.helpers.CONST;
import com.fgupioc.api_novasys.models.enums.EstadoSesionCaja;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 02/08/2026
 */
@Entity
@Table(name = "cajas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SQLDelete(sql = "UPDATE cajas SET deleted_at = NOW(6) WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Caja extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id")
    private Almacen almacen;
    private UUID uuid;
    private String code;
    private String name;
    private String description;
    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSesionCaja status;
    @OneToMany(mappedBy = "caja", fetch = FetchType.LAZY)
    private List<CajaSesion> sesiones = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = EstadoSesionCaja.CLOSED;
        }
    }
}

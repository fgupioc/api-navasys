package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */

@Entity
@Table(name = "usuario_almacen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "almacen"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@SQLDelete(sql = "UPDATE usuario_almacen SET deleted_at = NOW(6) WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class UsuarioAlmacen extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "almacen_id")
    private Almacen almacen;

    /** Almacén sugerido al abrir caja cuando el usuario tiene varios. */
    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "cod_status")
    private String codStatus;

    @PrePersist
    public void prePersist() {
        if (this.codStatus == null) {
            this.codStatus = CONST.STATUS_ACTIVE;
        }
    }
}
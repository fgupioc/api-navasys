package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.dtos.UnidadRespDTO;
import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "unidades")
@SQLDelete(sql = "UPDATE unidades SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Unidad extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    @Column(name = "cod_type")
    private String codType;
    @Column(name = "cod_status")
    private String codStatus;

    @PrePersist
    public void prePersist() {
        codStatus = CONST.STATUS_ACTIVE;
    }

    public static UnidadRespDTO toRespDTO(Unidad unidad) {
        return new UnidadRespDTO(
                unidad.getId(),
                unidad.getCode(),
                unidad.getName(),
                unidad.getCodType(),
                unidad.getCodStatus()
        );
    }
}

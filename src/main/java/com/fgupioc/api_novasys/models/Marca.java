package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.dtos.MarcaRespDTO;
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
@Table(name = "marcas")
@SQLDelete(sql = "UPDATE marcas SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Marca extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    @Column(name = "cod_status")
    private String codStatus;

    @PrePersist
    public void prePersist() {
        codStatus = CONST.STATUS_ACTIVE;
    }

    public static MarcaRespDTO toRespDTO(Marca marca) {
        return MarcaRespDTO.builder()
                .id(marca.getId())
                .code(marca.getCode())
                .name(marca.getName())
                .codStatus(marca.getCodStatus())
                .build();
    }
}

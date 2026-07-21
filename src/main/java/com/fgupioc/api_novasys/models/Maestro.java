package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.dtos.MaestroRespDTO;
import com.fgupioc.api_novasys.models.ids.MaestroId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "Maestro")
@Builder
public class Maestro {
    public final static Integer ESTADO = 1;
    public final static Integer TIPO_UNIDAD = 2;

    @EmbeddedId
    private MaestroId id;

    private String nombre;

    @Column(name = "valor_string")
    private String valorString;

    @Column(name = "valor_numerico")
    private Double valorNumerico;

    private Boolean activo;

    public static MaestroRespDTO toRespDTO(Maestro maestro) {
        return new MaestroRespDTO(
                maestro.getId().getCodigo(),
                maestro.getNombre(),
                maestro.getValorString(),
                maestro.getValorNumerico()
        );
    }
}

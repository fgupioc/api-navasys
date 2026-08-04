package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.enums.EstadoSesionCaja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 02/08/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CajaRespDTO {
    private UUID uuid;
    private String code;
    private String name;
    private String description;
    private EstadoSesionCaja status;
    private CajaSesionRespDTO cajaSesion;

    public CajaRespDTO(UUID uuid, String code, String name, String description, EstadoSesionCaja status) {
        this.uuid = uuid;
        this.code = code;
        this.name = name;
        this.description = description;
        this.status = status;
    }
}

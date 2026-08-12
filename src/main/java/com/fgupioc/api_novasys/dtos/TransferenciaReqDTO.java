package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.enums.EstadoTransferencia;
import lombok.*;

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

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaReqDTO {
    private UUID uuid;
    private Long almacenOrigenId;
    private Long almacenDestinoId;
    private String observation;
    private EstadoTransferencia codStatus;
    private List<TransferenciaDetalleReqDTO> productos = new ArrayList<>();
}

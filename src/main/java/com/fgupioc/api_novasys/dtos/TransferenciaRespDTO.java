package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.TransferenciaDetalle;
import com.fgupioc.api_novasys.models.User;
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
public class TransferenciaRespDTO {
    private Long almacenOrigenId;
    private String almacenOrigen;
    private Long almacenDestinoId;
    private String almacenDestino;
    private UUID uuid;
    private String code;
    private LocalDateTime fechaTransferencia;
    private EstadoTransferencia codStatus;
    private String status;
    private String observation;
    private String userName;
    private List<TransferenciaDetalleRespDTO> productos = new ArrayList<>();
}

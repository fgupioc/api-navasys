package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.TicketDTO;
import com.fgupioc.api_novasys.dtos.VentaReqDTO;
import com.fgupioc.api_novasys.dtos.VentaRespDTO;
import com.fgupioc.api_novasys.models.interfaces.VentaListado;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 27/07/2026
 */
public interface IVentaService {
    UUID registrar(VentaReqDTO ventaReqDTO, Authentication authentication) throws RuntimeException;

    UUID actualizar(UUID uuid, VentaReqDTO ventaReqDTO, Authentication authentication) throws RuntimeException;

    List<VentaListado> filtrarVentas(LocalDate desde, LocalDate hasta, Long customerId, String codStatus) throws RuntimeException;

    VentaRespDTO obtenerVentaPorUuid(UUID uuid) throws RuntimeException;

    void cancelar(UUID uuid) throws RuntimeException;

    void eliminar(UUID uuid) throws RuntimeException;

    TicketDTO getItemParTicket(UUID uuid) throws RuntimeException;
}

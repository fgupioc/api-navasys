package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.VentaReqDTO;
import com.fgupioc.api_novasys.models.interfaces.VentaListado;

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
    UUID registrar(VentaReqDTO ventaReqDTO) throws RuntimeException;

    UUID actualizar(UUID uuid, VentaReqDTO ventaReqDTO) throws RuntimeException;

    List<VentaListado> filtrarVentas(LocalDate desde, LocalDate hasta, Long customerId, String codStatus) throws RuntimeException;
}

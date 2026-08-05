package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.*;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 05/08/2026
 */
public interface ICajaService {

    List<CajaRespDTO> listarCajasPorAlmacen(Authentication authentication) throws RuntimeException;

    void abrirCaja(CajaSesionReqDTO cajaSesion, Authentication authentication) throws RuntimeException;

    void registrarCierreCaja(CajaSesionReqDTO cajaSesion, Authentication authentication) throws RuntimeException;

    CajaSesionRespDTO obtenerInfoParaCierreCaja(Authentication authentication) throws RuntimeException;

    List<CajaMovimientoRespDTO> listarMovimientosCaja(Authentication authentication) throws RuntimeException;

    void registrarCajaMovimiento(CajaMovimientoReqDTO cajaMovimiento, Authentication authentication) throws RuntimeException;

    void eliminarCajaMovimiento(Long cajaMovimientoId, Authentication authentication) throws RuntimeException;

    List<CajaMovimientoMotivoRespDTO> listarMotivosMovimientoCaja() throws RuntimeException;
}

package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.*;
import com.fgupioc.api_novasys.models.interfaces.ICajaCierreList;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
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

    List<ICajaCierreList> listarCierresCajaPorUsuario(Authentication authentication, LocalDate desde, LocalDate hasta, Long cajaId, String codStatus) throws RuntimeException;

    List<CajaRespDTO> listarCajas(Authentication authentication) throws RuntimeException;
}

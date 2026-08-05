package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.CajaMovimientoReqDTO;
import com.fgupioc.api_novasys.dtos.CajaSesionReqDTO;
import com.fgupioc.api_novasys.dtos.CajaSesionRespDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.ICajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 05/08/2026
 */

@RestController
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaController {
    private final ICajaService cajaService;

    @GetMapping("/listar-mis-cajas-por-almacen")
    public ResponseEntity<?> listarMisCajasPorAlmacen(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(cajaService.listarCajasPorAlmacen(authentication)));
    }

    @PostMapping("/abrir-caja")
    public ResponseEntity<?> abrirCaja(@RequestBody CajaSesionReqDTO cajaSesion, Authentication authentication) {
        cajaService.abrirCaja(cajaSesion, authentication);
        return ResponseEntity.ok(ApiResponse.success("Caja abierta exitosamente.", null));
    }

    @PutMapping("/registrar-cierre-caja")
    public ResponseEntity<?> registrarCierreCaja(@RequestBody CajaSesionReqDTO cajaSesion, Authentication authentication) {
        cajaService.registrarCierreCaja(cajaSesion, authentication);
        return ResponseEntity.ok(ApiResponse.success("Cierre de caja registrado exitosamente.", null));
    }

    @GetMapping("/obtener-info-para-cierre-caja")
    public ResponseEntity<?> obtenerInfoParaCierreCaja(Authentication authentication) {
        CajaSesionRespDTO cajaSesion = cajaService.obtenerInfoParaCierreCaja(authentication);
        return ResponseEntity.ok(ApiResponse.success("Información para cierre de caja obtenida exitosamente.", cajaSesion));
    }

    @GetMapping("/listar-mis-movimientos-caja")
    public ResponseEntity<?> listarMovimientosCaja(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(cajaService.listarMovimientosCaja(authentication)));
    }


    @PostMapping("/registrar-movimiento-caja")
    public ResponseEntity<?> registrarCajaMovimiento(@RequestBody CajaMovimientoReqDTO cajaMovimientoReqDTO, Authentication authentication) {
        cajaService.registrarCajaMovimiento(cajaMovimientoReqDTO, authentication);
        return ResponseEntity.ok(ApiResponse.success("Movimiento de caja registrado exitosamente.", null));
    }

    @DeleteMapping("/eliminar-movimiento-caja/{id}")
    public ResponseEntity<?> eliminarCajaMovimiento(@PathVariable Long id, Authentication authentication) {
        cajaService.eliminarCajaMovimiento(id, authentication);
        return ResponseEntity.ok(ApiResponse.success("Movimiento de caja eliminado exitosamente.", null));
    }

    @GetMapping("/listar-motivos-movimiento-caja")
    public ResponseEntity<?> listarMotivosMovimientoCaja() {
        return ResponseEntity.ok(ApiResponse.success(cajaService.listarMotivosMovimientoCaja()));
    }
}

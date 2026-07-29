package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.VentaReqDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.IVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 27/07/2026
 */

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {
    private final IVentaService ventaService;

    @GetMapping("/filtro")
    public ResponseEntity<?> filtrarVentas(@RequestParam(required = false) LocalDate desde, @RequestParam(required = false) LocalDate hasta, @RequestParam(required = false) Long customerId, @RequestParam(required = false) String codStatus) {
        return ResponseEntity.ok(ApiResponse.success(ventaService.filtrarVentas(desde, hasta, customerId, codStatus)));
    }

    @GetMapping("/buscar/{uuid}")
    public ResponseEntity<?> obtenerVentaPorUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(ventaService.obtenerVentaPorUuid(uuid)));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarVenta(@RequestBody VentaReqDTO ventaReqDTO) {
        return ResponseEntity.ok(ApiResponse.success(ventaService.registrar(ventaReqDTO)));
    }

    @PutMapping("/actualizar/{uuid}")
    public ResponseEntity<?> actualizarVenta(@PathVariable UUID uuid, @RequestBody VentaReqDTO ventaReqDTO) {
        return ResponseEntity.ok(ApiResponse.success(ventaService.actualizar(uuid, ventaReqDTO)));
    }

    @PutMapping("/cancelar/{uuid}")
    public ResponseEntity<?> cancelarVenta(@PathVariable UUID uuid) {
        ventaService.cancelar(uuid);
        return ResponseEntity.ok(ApiResponse.success("Venta cancelada exitosamente.", null));
    }
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> eliminarVenta(@PathVariable UUID uuid) {
        ventaService.eliminar(uuid);
        return ResponseEntity.ok(ApiResponse.success("Venta eliminada exitosamente.", null));
    }
}


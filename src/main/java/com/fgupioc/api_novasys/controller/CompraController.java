package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.CompraReqDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.ICompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {
    private final ICompraService compraService;

    @GetMapping("/buscar/{uuid}")
    public ResponseEntity<?> buscarCompra(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(compraService.buscarCompra(uuid)));
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarCompras() {
        return ResponseEntity.ok(ApiResponse.success(compraService.listarCompras()));
    }

    @GetMapping("/filtro")
    public ResponseEntity<?> filtrarCompras(@RequestParam(required = false) LocalDate desde, @RequestParam(required = false) LocalDate hasta, @RequestParam(required = false) Long supplierId, @RequestParam(required = false) String codStatus) {
        return ResponseEntity.ok(ApiResponse.success(compraService.filtrarCompras(desde, hasta, supplierId, codStatus)));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCompra(@RequestBody CompraReqDTO compraReqDTO) {
        return ResponseEntity.ok(ApiResponse.success(compraService.registrarCompra(compraReqDTO)));
    }

    @PutMapping("/actualizar/{uuid}")
    public ResponseEntity<?> actualizarCompra(@PathVariable UUID uuid, @RequestBody CompraReqDTO compraReqDTO) {
        return ResponseEntity.ok(ApiResponse.success(compraService.actualizarCompra(uuid, compraReqDTO)));
    }

    @GetMapping("/{uuid}/orden-compra/pdf")
    public ResponseEntity<byte[]> descargarOrdenCompra(@PathVariable UUID uuid) throws Exception {
        byte[] pdf = compraService.generar(uuid);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=orden-compra.pdf")
                .body(pdf);
    }

    @PutMapping("/anular/{uuid}")
    public ResponseEntity<?> anularCompra(@PathVariable UUID uuid) {
        compraService.anularCompra(uuid);
        return ResponseEntity.ok(ApiResponse.success("Compra anulada correctamente", null));
    }
}

package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.CompraReqDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.ICompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCompra(@RequestBody CompraReqDTO compraReqDTO) {
        return ResponseEntity.ok(ApiResponse.success(compraService.registrarCompra(compraReqDTO)));
    }
}

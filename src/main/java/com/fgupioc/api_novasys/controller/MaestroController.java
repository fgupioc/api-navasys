package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.IMaestroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */
@RestController
@RequestMapping("/api/maestro")
@RequiredArgsConstructor
public class MaestroController {
    private final IMaestroService maestroService;

    @GetMapping("/estados")
    public ResponseEntity<?> listarEstados() {
        return ResponseEntity.ok(ApiResponse.success(maestroService.listarEstados()));
    }

    @GetMapping("/tipo-unidades")
    public ResponseEntity<?> listarTipoUnidades() {
        return ResponseEntity.ok(ApiResponse.success(maestroService.listarTipoUnidades()));
    }
}

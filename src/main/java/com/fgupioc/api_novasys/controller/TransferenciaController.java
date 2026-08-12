package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.TransferenciaReqDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.ITransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 09/08/2026
 */
@RestController
@RequestMapping("/api/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {
    private final ITransferenciaService transferenciaService;

    @PostMapping(value = "registrar")
    public ResponseEntity<?> registrarTransferencia(@RequestBody TransferenciaReqDTO transferenciaReqDTO, Authentication authentication) throws RuntimeException {
        return ResponseEntity.ok(ApiResponse.success("Producto creado exitosamente", transferenciaService.registrarTransferencia(transferenciaReqDTO, authentication)));
    }

    @PostMapping(value = "actualizar/{uuid}")
    public ResponseEntity<?> actualizarTransferencia(@PathVariable UUID uuid, @RequestBody TransferenciaReqDTO transferenciaReqDTO, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado exitosamente", transferenciaService.actualizarTransferencia(uuid, transferenciaReqDTO, authentication)));
    }
}

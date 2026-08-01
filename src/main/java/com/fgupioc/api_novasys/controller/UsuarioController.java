package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final IUsuarioService usuarioService;

    @GetMapping("/findByDocument")
    public ResponseEntity<?> findByDocument(@RequestParam String document) {
        return ResponseEntity.ok(usuarioService.findByDocument(document));
    }

    @GetMapping("mis-almacenes")
    public ResponseEntity<?> listarAlmacenesPorUsuarioLogueado(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(usuarioService.almacenes(authentication)));
    }

    @GetMapping("mi-almacen")
    public ResponseEntity<?> obtenerMiAlmacen(Authentication authentication, @RequestParam Long almacenId) {
        return ResponseEntity.ok(ApiResponse.success(usuarioService.miAlmacen(authentication, almacenId)));
    }
}

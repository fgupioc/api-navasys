package com.fgupioc.api_novasys.security;

import com.fgupioc.api_novasys.dtos.ContextoResponse;
import com.fgupioc.api_novasys.security.models.AuthResponse;
import com.fgupioc.api_novasys.security.models.LoginRequest;
import com.fgupioc.api_novasys.security.models.RefreshTokenRequest;
import com.fgupioc.api_novasys.security.models.RegisterRequest;
import com.fgupioc.api_novasys.security.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final IAuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getUsername());

        try {
            AuthResponse authResponse = authService.login(loginRequest);
            log.info("Login exitoso para usuario: {}", loginRequest.getUsername());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            log.error("Error en login para usuario {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) throws Exception {
        try {
            return ResponseEntity.ok(authService.register(registerRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Refresh token", description = "Obtener nuevo access token usando refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Parameter(description = "Refresh token") @RequestBody RefreshTokenRequest request) {
        try {
            return ResponseEntity.ok(authService.validateRefreshToken(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

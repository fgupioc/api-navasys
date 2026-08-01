package com.fgupioc.api_novasys.security.service;

import com.fgupioc.api_novasys.dtos.ContextoResponse;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.security.models.AuthResponse;
import com.fgupioc.api_novasys.security.models.LoginRequest;
import com.fgupioc.api_novasys.security.models.RefreshTokenRequest;
import com.fgupioc.api_novasys.security.models.RegisterRequest;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface IAuthService {
    AuthResponse login(LoginRequest loginRequest) throws Exception;

    AuthResponse register(RegisterRequest registerRequest) throws Exception;

    AuthResponse validateRefreshToken(RefreshTokenRequest refreshToken) throws Exception;

    ContextoResponse generarTokenParaAlmacen(Authentication authentication, UUID almacenUuid) throws Exception;
}

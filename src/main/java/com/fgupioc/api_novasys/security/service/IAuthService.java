package com.fgupioc.api_novasys.security.service;

import com.fgupioc.api_novasys.security.models.AuthResponse;
import com.fgupioc.api_novasys.security.models.LoginRequest;
import com.fgupioc.api_novasys.security.models.RefreshTokenRequest;
import com.fgupioc.api_novasys.security.models.RegisterRequest;

public interface IAuthService {
    AuthResponse login(LoginRequest loginRequest) throws Exception;

    AuthResponse register(RegisterRequest registerRequest) throws Exception;

    AuthResponse validateRefreshToken(RefreshTokenRequest refreshToken) throws Exception;


}

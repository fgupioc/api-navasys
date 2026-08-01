package com.fgupioc.api_novasys.security.service;

import com.fgupioc.api_novasys.models.User;
import com.fgupioc.api_novasys.security.models.UserView;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface IJwtService {
    String generateToken(User user) throws RuntimeException;

    String generateRefreshToken(User user) throws RuntimeException;
    String generateRefreshToken(User user, UUID sucursalUUid) throws RuntimeException;

    String getTokenFormRequest(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean validateRefreshToken(String refreshToken);

    String getDocumentFromRefreshToken(String refreshToken);

    UserView decodeToken(String token);

    String generateTokenWithSucursal(User user, UUID sucursalUUid) throws RuntimeException;
}

package com.fgupioc.api_novasys.security.service;

import com.fgupioc.api_novasys.models.Rol;
import com.fgupioc.api_novasys.models.User;
import com.fgupioc.api_novasys.repository.RolRepository;
import com.fgupioc.api_novasys.repository.UserRepository;
import com.fgupioc.api_novasys.security.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServer implements IAuthService {
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest loginRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userRepository.login(loginRequest.getUsername())
                .orElseThrow(() -> new Exception("User not found"));
        /*
        if (loginRequest.getType() != null) {
            if (!user.getRol().getName().equalsIgnoreCase(loginRequest.getType())) {
                throw new Exception("User role does not match the login type");
            }
        }
        */
        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) throws Exception {
        try {
            Rol role = rolRepository.findById(registerRequest.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            User user = User.builder()
                    .userId(1l)
                    .document(registerRequest.getDocument())
                    .phone(registerRequest.getPhone())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .name(registerRequest.getName())
                    .lastname(registerRequest.getLastname())
                    .rol(role)
                    .build();
            userRepository.save(user);
            return AuthResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthResponse validateRefreshToken(RefreshTokenRequest request) throws Exception {
        if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        String document = jwtService.getDocumentFromRefreshToken(request.getRefreshToken());
        User user = userRepository.findByDocument(document)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar nuevos tokens
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public static UserView getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserView)) {
            return UserView.builder().id(null).build();
        }
        UserView view = (UserView) auth.getPrincipal();
        if (view == null) {
            return UserView.builder().id(null).build();
        }
        return view;
    }
}

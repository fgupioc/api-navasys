package com.fgupioc.api_novasys.security.service;

import com.fgupioc.api_novasys.helpers.CONST;
import com.fgupioc.api_novasys.models.Role;
import com.fgupioc.api_novasys.models.User;
import com.fgupioc.api_novasys.models.UserPrincipal;
import com.fgupioc.api_novasys.repository.PermissionRepository;
import com.fgupioc.api_novasys.repository.RoleRepository;
import com.fgupioc.api_novasys.repository.UserRepository;
import com.fgupioc.api_novasys.security.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServer implements IAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IJwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        /**
         *
         * Set<String> permisos = new HashSet<>(permissionRepository.codigosPorRol(rolId));
         * userPermissionRepository.findByUserId(userId).forEach(up -> {
         *     if (Boolean.TRUE.equals(up.getGranted())) permisos.add(up.getPermission().getCode());
         *     else                                      permisos.remove(up.getPermission().getCode());
         * });
         */

        User user = userRepository.findById(principal.id())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) throws Exception {
        try {
            Role role = roleRepository.findById(registerRequest.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            User user = User.builder()
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

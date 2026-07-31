package com.fgupioc.api_novasys.security.config;


import com.fgupioc.api_novasys.models.UserPrincipal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtil {

    public static UserPrincipal principal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new IllegalStateException("No hay un usuario autenticado en el contexto");
        }
        return principal;
    }

    public static Long usuarioId() {
        return principal().id();
    }

    public static Long tiendaId() {
        return principal().branchId();
    }

    public static boolean tienePermiso(String code) {
        return principal().permisos().contains(code);
    }
}
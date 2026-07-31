package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.models.enums.Alcance;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public record UserPrincipal(
        Long id,
        Long branchId,
        String document,
        String password,
        String rolName,
        Alcance alcance,
        Set<String> permisos,
        boolean activo
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(
                        Stream.of("ROLE_" + rolName),
                        permisos.stream())
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override public String getUsername() { return document; }
    @Override public String getPassword() { return password; }
    @Override public boolean isEnabled() { return activo; }
}
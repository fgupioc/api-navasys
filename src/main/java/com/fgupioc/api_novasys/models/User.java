package com.fgupioc.api_novasys.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails {
    public static final Long CUSTOMER = 2L; // usuario cliente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Rol rol;
    private String name;
    @Column(name = "last_name")
    private String lastname;
    private String password;
    @Column(unique = true, nullable = false)
    private String document;
    private String phone;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "cod_status")
    private String codStatus;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime registrationDate;

    @PrePersist
    public void prePersist() {
        this.codStatus = CONST.STATUS_ACTIVE;
        this.registrationDate = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.getName()));
    }

    @Override
    public String getUsername() {
        return this.document; // Ahora retorna el email
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
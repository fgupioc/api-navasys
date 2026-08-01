package com.fgupioc.api_novasys.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.*;
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

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"rol", "password"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private Role rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Tienda tienda;

    private String name;

    @Column(name = "last_name")
    private String lastname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String document;

    private String phone;

    @Column(name = "cod_status")
    private String codStatus;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @PrePersist
    public void prePersist() {
        this.codStatus = CONST.STATUS_ACTIVE;
        this.registrationDate = LocalDateTime.now();
    }

    public String getStatus() {
        return CONST.STATUS_ACTIVE.equalsIgnoreCase(codStatus) ? "Activo" : "Inactivo";
    }
}
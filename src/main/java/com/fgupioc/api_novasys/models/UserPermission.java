package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.models.ids.UserPermissionId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */

@Entity
@Table(name = "user_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "permission"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPermission {

    @EmbeddedId
    @EqualsAndHashCode.Include
    @Builder.Default
    private UserPermissionId id = new UserPermissionId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private Permission permission;

    /**
     * true  = concede un permiso que el rol no trae.
     * false = revoca un permiso que el rol sí trae.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean granted = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static UserPermission grant(User user, Permission permission) {
        return UserPermission.builder().user(user).permission(permission).granted(true).build();
    }

    public static UserPermission revoke(User user, Permission permission) {
        return UserPermission.builder().user(user).permission(permission).granted(false).build();
    }
}
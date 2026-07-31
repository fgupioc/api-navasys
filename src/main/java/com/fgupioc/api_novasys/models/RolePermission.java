package com.fgupioc.api_novasys.models;
import com.fgupioc.api_novasys.models.ids.RolePermissionId;
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
@Table(name = "role_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"role", "permission"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RolePermission {
    @EmbeddedId
    @EqualsAndHashCode.Include
    @Builder.Default
    private RolePermissionId id = new RolePermissionId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static RolePermission of(Role role, Permission permission) {
        return RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();
    }
}

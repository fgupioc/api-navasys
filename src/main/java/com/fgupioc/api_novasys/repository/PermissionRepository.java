package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(value = """
            SELECT p.code
            FROM role_permissions rp
            JOIN permissions p ON p.id = rp.permission_id
            WHERE rp.role_id = :rolId
              AND NOT EXISTS (
                  SELECT 1 FROM user_permissions up
                  WHERE up.user_id       = :userId
                    AND up.permission_id = rp.permission_id
                    AND up.granted       = 0
              )
            UNION
            SELECT p.code
            FROM user_permissions up
            JOIN permissions p ON p.id = up.permission_id
            WHERE up.user_id = :userId
              AND up.granted = 1
            """, nativeQuery = true)
    Set<String> resolverPorUsuario(@Param("userId") Long userId, @Param("rolId") Long rolId);

    @Query("SELECT rp.permission.code FROM RolePermission rp WHERE rp.role.id = :rolId")
    Set<String> codigosPorRol(@Param("rolId") Long rolId);
}

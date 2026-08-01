package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.User;
import com.fgupioc.api_novasys.models.UsuarioAlmacen;
import com.fgupioc.api_novasys.models.interfaces.IUsuarioAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioAlmacenRepository extends JpaRepository<UsuarioAlmacen, Long> {
    List<UsuarioAlmacen> findByUser(User user);

    @Query(value = """
            select ua.id as userWarehouseId,
                   ua.is_default as isDefault,
                   a.name as warehouseName,
                    BIN_TO_UUID(a.uuid) as warehouseUuid,
                   a.code as warehouseCode,
                   a.description as warehouseDescription
            from usuario_almacen ua
                     inner join almacenes a on a.id = ua.almacen_id
            where ua.user_id = :userId
              and ua.cod_status = '1'
              and a.cod_status = '1'
              and ua.deleted_at is null;
            """, nativeQuery = true)
    List<IUsuarioAlmacen> findByUserId(Long userId);

    @Query(value = """
            select ua.id as userWarehouseId,
                   ua.is_default as isDefault,
                   a.name as warehouseName,
                    BIN_TO_UUID(a.uuid) as warehouseUuid,
                   a.code as warehouseCode,
                   a.description as warehouseDescription
            from usuario_almacen ua
                     inner join almacenes a on a.id = ua.almacen_id
            where ua.user_id = :userId and ua.almacen_id = :almacenId
              and ua.cod_status = '1'
              and a.cod_status = '1'
              and ua.deleted_at is null;
            """, nativeQuery = true)
    Optional<IUsuarioAlmacen> findByUserIdAndAlmacenId(Long userId, Long almacenId);
}

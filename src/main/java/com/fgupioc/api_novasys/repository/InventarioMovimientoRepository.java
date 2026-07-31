package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.InventarioMovimiento;
import com.fgupioc.api_novasys.models.interfaces.ProductoAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventarioMovimientoRepository extends JpaRepository<InventarioMovimiento, Long> {
    void deleteAllByReferenceId(Long id);

    @Query(value = """
            select a.id                     as warehouseId,
                   a.name                   as warehouseName,
                   im.product_id            as productId,
                   im.cod_status            as codStatus,
                   sum(im.current_quantity) as stock
            from inventario_movimiento im
                     inner join productos p on im.product_id = p.id
                     inner join almacenes a on a.id = im.warehouse_id
                     inner join tiendas t on t.id = a.branch_id
            where t.id = :tiendaId
              and im.product_id = :productoId
              and im.reference_type = 'PURCHASE'
              and im.current_quantity > 0
              and p.cod_status = '1'
              and p.deleted_at is null
            group by a.id, a.name, im.product_id, im.cod_status;
            """, nativeQuery = true)
    List<ProductoAlmacen> listarProductosAlmacenByAlmacenId(Long tiendaId, Long productoId);

    @Modifying
    @Query("update InventarioMovimiento im set im.codStatus = :codStatus where im.almacen.id = :warehouseId and im.producto.id = :id")
    void updateStatus(Long warehouseId, Long id, String codStatus);
}

package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.InventarioMovimiento;
import com.fgupioc.api_novasys.models.Producto;
import com.fgupioc.api_novasys.models.enums.InventarioMovimientoTipo;
import com.fgupioc.api_novasys.models.interfaces.IHistorialKardex;
import com.fgupioc.api_novasys.models.interfaces.IKardexProductoInfo;
import com.fgupioc.api_novasys.models.interfaces.ProductoAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventarioMovimientoRepository extends JpaRepository<InventarioMovimiento, Long> {
    void deleteAllByReferenceId(Long id);

    @Query(value = """
              select a.id                   as warehouseId,
                   BIN_TO_UUID(a.uuid)      as warehouseUuid,
                   a.name                   as warehouseName,
                   ins.product_id            as productId,
                   ins.cod_status            as codStatus,
                   sum(ins.quantity) as stock
            from   novasys.inventario_stock as ins
                     inner join productos p on ins.product_id = p.id
                     inner join almacenes a on a.id = ins.warehouse_id
                     inner join tiendas t on t.id = a.branch_id
            where t.id = :tiendaId
              and ins.product_id = :productoId 
              and ins.quantity > 0
              and p.cod_status = '1'
              and p.deleted_at is null
            group by a.id, a.name, ins.product_id, ins.cod_status, warehouseUuid;
            """, nativeQuery = true)
    List<ProductoAlmacen> listarProductosAlmacenByAlmacenId(Long tiendaId, Long productoId);

    @Modifying
    @Query("update InventarioMovimiento im set im.codStatus = :codStatus where im.almacen.id = :warehouseId and im.producto.id = :id")
    void updateStatus(Long warehouseId, Long id, String codStatus);

    @Query("select im from InventarioMovimiento im where im.producto = :producto and im.almacen = :almacen and im.tipoMovimiento = :inventarioMovimientoTipo")
    Optional<InventarioMovimiento> findByProductoAndAlmacenAndInventarioMovimientoTipo(Producto producto, Almacen almacen, InventarioMovimientoTipo inventarioMovimientoTipo);

    @Query(value = """
            SELECT p.id                                       AS productId,
                   p.name                                     AS productName,
                   p.code                                     AS productCode,
                   u.name                                     AS unitName,
                   u.code                                     AS unitCode,
                   a.name                                     AS warehouseName,
                   pi.url,
            
                   ins.initial_quantity                       AS initialQuantity,
                   mov.total_entries                          AS totalEntries,
                   mov.sold_quantity                          AS soldQuantity,
                   ins.quantity                               AS currentQuantity,
            
                   COALESCE(pre.sale_price, 0) * ins.quantity AS totalValue,
                   COALESCE(pre.sale_price, 0)                AS salePrice,
                   COALESCE(pre.regular_price, 0)             AS regularPrice,
                   COALESCE(pre.wholesale_price, 0)           AS wholesalePrice
            
            FROM   inventario_stock ins
                       INNER JOIN productos p ON p.id = ins.product_id
                       INNER JOIN almacenes a ON a.id = ins.warehouse_id
                       INNER JOIN unidades  u ON u.id = p.unit_id
            
                       LEFT JOIN LATERAL (
                SELECT pi2.url
                FROM   producto_imagenes pi2
                WHERE  pi2.producto_id = p.id
                  AND  pi2.es_principal = 1
                ORDER  BY pi2.id
                LIMIT  1
                ) pi ON TRUE
            
                       LEFT JOIN LATERAL (
                SELECT COALESCE(SUM(CASE WHEN im.movement_type = 'PURCHASE_IN'
                                             THEN im.quantity END), 0) AS total_entries,
                       COALESCE(SUM(CASE WHEN im.movement_type = 'SALE_OUT'
                                             THEN im.quantity END), 0) AS sold_quantity
                FROM   inventario_movimiento im
                WHERE  im.product_id   = ins.product_id
                  AND  im.warehouse_id = ins.warehouse_id
                  AND  im.cod_status   = '1'
                ) mov ON TRUE
            
                       LEFT JOIN LATERAL (
                SELECT MAX(CASE WHEN pr.price_type = 'SALE'      THEN pr.sale_price END) AS sale_price,
                       MAX(CASE WHEN pr.price_type = 'REGULAR'   THEN pr.sale_price END) AS regular_price,
                       MAX(CASE WHEN pr.price_type = 'WHOLESALE' THEN pr.sale_price END) AS wholesale_price
                FROM   producto_precios pr
                WHERE  pr.producto_id = p.id
                  AND  pr.almacen_id  = a.id
                  AND  pr.cod_status  = '1'
                  AND  pr.price_type IN ('SALE','REGULAR','WHOLESALE')
                ) pre ON TRUE
            
            WHERE p.cod_status  = '1'
              AND p.deleted_at IS NULL
              AND ins.warehouse_id = :almacenId
              AND ins.product_id   = :productoId limit 1
            """, nativeQuery = true)
    Optional<IKardexProductoInfo> obtenerKardexProductoInfoByProductoId(Long productoId, Long almacenId);


    @Query("SELECT im FROM InventarioMovimiento im WHERE im.producto = :producto AND im.almacen = :almacen ORDER BY im.id DESC limit 1")
    Optional<InventarioMovimiento> findLastByProductoAndAlmacen(Producto producto, Almacen almacen);

    @Query(value = """
            
            SELECT im.reference_type                                                                                            as referenceType,
                   COALESCE(DATE_FORMAT(c.created_at, '%d/%m/%Y %h:%i %p'), DATE_FORMAT(v.created_at, '%d/%m/%Y %h:%i %p'), '') AS createdAt,
                   COALESCE(c.code, v.code, '')                                                                                 AS document,
                   mr2.nombre                                                                                                   AS type,
                   mr.nombre                                                                                                    AS reference,
            
                   CASE
                       WHEN im.reference_type IN ('PURCHASE')
                           THEN im.quantity
                       ELSE 0 END                                                                                               AS entries,
                   CASE
                       WHEN im.reference_type IN ('SALE')
                           THEN im.quantity
                       ELSE 0 END                                                                                               AS exits,
                   im.unit_cost                                                                                                 AS unitCost,
                   im.quantity * im.unit_cost                                                                                   AS totalCost,
                   u.name                                                                                                       AS userName
            
            FROM inventario_movimiento im
                     INNER JOIN users u ON u.id = im.user_id
                     INNER JOIN maestro mr ON mr.tabla_id = 7 AND mr.codigo = im.reference_type
                     INNER JOIN maestro mr2 ON mr2.tabla_id = 8 AND mr2.codigo = im.movement_type
            
                     LEFT JOIN compras c
                               ON im.reference_type = 'PURCHASE'
                                   AND c.id = im.reference_id
            
                     LEFT JOIN ventas v
                               ON im.reference_type = 'SALE'
                                   AND v.id = im.reference_id
            
            WHERE im.warehouse_id = :almacenId
              AND im.product_id = :productoId
              AND im.cod_status = '1'
            ORDER BY COALESCE(c.created_at, v.created_at, '') desc;
            """, nativeQuery = true)
    List<IHistorialKardex> findAllHistorialKardexByProductoIdAndAlmacenId(Long productoId, Long almacenId);
}

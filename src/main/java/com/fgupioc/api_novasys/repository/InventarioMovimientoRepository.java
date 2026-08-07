package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.InventarioMovimiento;
import com.fgupioc.api_novasys.models.Producto;
import com.fgupioc.api_novasys.models.enums.InventarioMovimientoTipo;
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
            select a.id                     as warehouseId,
                   BIN_TO_UUID(a.uuid)       as warehouseUuid,
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
            group by a.id, a.name, im.product_id, im.cod_status, warehouseUuid;
            """, nativeQuery = true)
    List<ProductoAlmacen> listarProductosAlmacenByAlmacenId(Long tiendaId, Long productoId);

    @Modifying
    @Query("update InventarioMovimiento im set im.codStatus = :codStatus where im.almacen.id = :warehouseId and im.producto.id = :id")
    void updateStatus(Long warehouseId, Long id, String codStatus);

    @Modifying
    @Query("update InventarioMovimiento im set im.currentQuantity = im.currentQuantity - :quantity where im.almacen.id = :warehouseId and im.producto.id = :productId")
    void restarStock(Long warehouseId, Long productId, Double quantity);

    @Query("select im from InventarioMovimiento im where im.producto = :producto and im.almacen = :almacen and im.tipoMovimiento = :inventarioMovimientoTipo")
    Optional<InventarioMovimiento> findByProductoAndAlmacenAndInventarioMovimientoTipo(Producto producto, Almacen almacen, InventarioMovimientoTipo inventarioMovimientoTipo);

    @Query(value = """
            select p.id as productId,
                   p.name as productName,
                   p.code as productCode,
                   u.name as unitName,
                   u.code as unitCode,
                   a.name as warehouseName,
                   pi.url,
                   im.initial_quantity as initialQuantity,
                   im.current_quantity as currentQuantity,
                   im.initial_quantity - im.current_quantity as soldQuantity,
                   im.sale_price * im.current_quantity as totalValue,
                    COALESCE(pp.sale_price, 0)                AS salePrice,
                  COALESCE(ppr.sale_price, 0)               AS regularPrice,
                  COALESCE(ppw.sale_price, 0)               AS wholesalePrice
            from inventario_movimiento im
                     inner join productos p on im.product_id = p.id
                     inner join almacenes a on a.id = im.warehouse_id
                     inner join unidades u on u.id = p.unit_id
                     left join lateral (
                select pi2.url
                from producto_imagenes pi2
                where pi2.producto_id = p.id
                  and pi2.es_principal = 1
                order by pi2.id
                limit 1
                ) pi on true
              LEFT JOIN producto_precios pp
                               ON pp.producto_id = p.id
                                   AND pp.almacen_id = a.id
                                   AND pp.price_type = 'SALE'
                                   AND pp.cod_status = '1'
                     LEFT JOIN producto_precios ppr
                               ON ppr.producto_id = p.id
                                   AND ppr.almacen_id = a.id
                                   AND ppr.price_type = 'REGULAR'
                                   AND ppr.cod_status = '1'
                     LEFT JOIN producto_precios ppw
                               ON ppw.producto_id = p.id
                                   AND ppw.almacen_id = a.id
                                   AND ppw.price_type = 'WHOLESALE'
                                   AND ppw.cod_status = '1'
            where im.reference_type = 'PURCHASE'
              and p.cod_status = '1'
              and p.deleted_at is null
              and im.warehouse_id = :almacenId
              and im.product_id = :productoId
                        limit 1
            """, nativeQuery = true)
    Optional<IKardexProductoInfo> obtenerKardexProductoInfoByProductoId(Long productoId, Long almacenId);


}

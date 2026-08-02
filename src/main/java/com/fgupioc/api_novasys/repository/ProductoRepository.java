package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Producto;
import com.fgupioc.api_novasys.models.interfaces.IProductoAlmacenList;
import com.fgupioc.api_novasys.models.interfaces.ProductoAutocompledado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByCode(String code);


    @Query(value = "SELECT * FROM productos WHERE deleted_at IS NULL order by id desc", nativeQuery = true)
    List<Producto> findAllByOrderByIdDesc();

    @Query(value = "SELECT * FROM productos WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Producto> findAllTrashed();

    @Query(value = "SELECT * FROM productos", nativeQuery = true)
    List<Producto> findAllWithTrashed();

    @Modifying
    @Query(value = "UPDATE productos SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);

    @Modifying
    @Query(value = "update productos u set u.deleted_at = null where u.id = :id", nativeQuery = true)
    void resetDeletedAt(Long id);

    @Query("select count(p) from Producto p")
    Integer countProductos();

    Optional<Producto> findByUuid(UUID uuid);

    @Query(value = """
            select p.id            as id,
                   p.code          as code,
                   p.name          as name,
                   p.barcode       as barcode,
                   pi.url          as url,
                   u.name          as unidad,
                   c.name          as categoria,
                   sc.name         as subCategoria
            from productos p
                     inner join unidades u on p.unit_id = u.id
                     inner join categorias c on p.category_id = c.id
                     inner join categorias sc on p.subcategory_id = sc.id
                     left join lateral (
                         select pi2.url
                         from producto_imagenes pi2
                         where pi2.producto_id = p.id and pi2.es_principal = 1
                         order by pi2.id
                         limit 1
                     ) pi on true
            where (:term is null or trim(upper(concat(p.code, ' ', p.name))) like concat('%', trim(upper(:term)), '%'))
            order by p.name
            limit 20
            """, nativeQuery = true)
    List<ProductoAutocompledado> buscarProductos(String term);

    @Query(value = """
            
            select p.id                as productId,
                   p.code,
                   p.name,
                   p.barcode,
                   p.unit_id           as unitId,
                   u.name              as unidad,
                   p.category_id       as categoryId,
                   c.name              as categoria,
                   p.subcategory_id    as subcategoryId,
                   sc.name             as subCategoria,
                   im.current_quantity as stock,
                   pp.sale_price,
                   pp.price_type,
                   im.cod_status,
                   ms.nombre           as status
            from inventario_movimiento im
                     inner join almacenes a on a.id = im.warehouse_id
                     inner join productos p on p.id = im.product_id
                     join unidades u on u.id = p.unit_id
                     join categorias c on c.id = p.category_id
                     left join categorias sc on sc.id = p.subcategory_id
                     left join producto_precios pp on pp.producto_id = p.id and pp.price_type = 'SALE' and pp.almacen_id = a.id
                     inner join maestro ms on ms.tabla_id = 1 and ms.codigo = im.cod_status
            where im.reference_type = 'PURCHASE'
              and BIN_TO_UUID(a.uuid) = :almacenUuid
            """, nativeQuery = true)
    List<IProductoAlmacenList> findProductosByAlmacenId(@Param("almacenUuid") String almacenUuid);
}

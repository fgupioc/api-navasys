package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Producto;
import com.fgupioc.api_novasys.models.interfaces.ProductoAutocompledado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
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
}

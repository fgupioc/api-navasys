package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Producto;
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
}

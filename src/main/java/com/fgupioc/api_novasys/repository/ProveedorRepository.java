package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    @Query(value = "SELECT * FROM proveedores WHERE deleted_at IS NULL order by id desc", nativeQuery = true)
    List<Proveedor> findAllByOrderByIdDesc();

    @Query(value = "SELECT * FROM proveedores WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Proveedor> findAllTrashed();

    @Query(value = "SELECT * FROM proveedores", nativeQuery = true)
    List<Proveedor> findAllWithTrashed();

    @Modifying
    @Query(value = "UPDATE proveedores SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);

    @Modifying
    @Query(value = "update proveedores u set u.deleted_at = null where u.id = :id", nativeQuery = true)
    void resetDeletedAt(Long id);

    Optional<Proveedor> findByRuc(String ruc);

    @Query(value = "SELECT * FROM proveedores WHERE ruc = :ruc AND id <> :id", nativeQuery = true)
    Optional<Proveedor> findByRucDistinct(@Param("ruc") String ruc, @Param("id") Long id);
}

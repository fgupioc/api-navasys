package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Unidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnidadRepository extends JpaRepository<Unidad, Long> {
    @Query(value = "SELECT * FROM unidades WHERE deleted_at IS NULL order by id desc", nativeQuery = true)
    List<Unidad> findAllByOrderByIdDesc();

    @Query(value = "SELECT * FROM unidades WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Unidad> findAllTrashed();

    @Query(value = "SELECT * FROM unidades", nativeQuery = true)
    List<Unidad> findAllWithTrashed();

    @Modifying
    @Query(value = "UPDATE unidades SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);

    @Modifying
    @Query(value = "update unidades u set u.deleted_at = null where u.id = :id", nativeQuery = true)
    void resetDeletedAt(Long id);

    Optional<Unidad> findByCode(String code);

    boolean existsByCode(String code);
}

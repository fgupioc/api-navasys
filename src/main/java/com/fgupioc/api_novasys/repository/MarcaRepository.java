package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    @Query(value = "SELECT * FROM marcas WHERE deleted_at IS NULL order by id desc", nativeQuery = true)
    List<Marca> findAllByOrderByIdDesc();

    @Query(value = "SELECT * FROM marcas WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Marca> findAllTrashed();

    @Query(value = "SELECT * FROM marcas", nativeQuery = true)
    List<Marca> findAllWithTrashed();

    @Modifying
    @Query(value = "UPDATE marcas SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);

    @Modifying
    @Query(value = "update marcas u set u.deleted_at = null where u.id = :id", nativeQuery = true)
    void resetDeletedAt(Long id);

    Optional<Marca> findByCode(String code);

    boolean existsByCode(String code);
}

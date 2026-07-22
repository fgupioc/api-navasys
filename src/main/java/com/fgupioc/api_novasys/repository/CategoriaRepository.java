package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query(value = "SELECT * FROM categorias WHERE deleted_at IS NULL and parent_id is null order by id desc", nativeQuery = true)
    List<Categoria> findAllPadresByOrderByIdDesc();

    @Query(value = "SELECT * FROM categorias WHERE deleted_at IS NULL  order by id desc", nativeQuery = true)
    List<Categoria> findAllByOrderByIdDesc();

    @Query("select c from Categoria c where upper(c.code) = upper(:code)")
    Optional<Categoria> findByCode(String code);

    @Query("select c from Categoria c where upper(c.code) = upper(:code) and c.id <> :id")
    Optional<Categoria> findByCodeOther(String code, Long id);
}

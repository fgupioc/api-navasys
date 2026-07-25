package com.fgupioc.api_novasys.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.fgupioc.api_novasys.models.Maestro;
import com.fgupioc.api_novasys.models.ids.MaestroId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */
public interface MaestroRepository extends JpaRepository<Maestro, MaestroId> {
    @Query(value = "SELECT m FROM Maestro m WHERE m.id.tablaId = ?1 and m.activo = true")
    List<Maestro> findByIdTablaId(Integer tablaId);

    boolean existsByIdTablaIdAndIdCodigo(Integer tablaId, String codigo);

    @Query("select m from Maestro m where m.id.tablaId = ?1 and m.id.codigo = ?2 ")
    Optional<Maestro> findByTablaIdAndCodigo(int tablaId, String codStatus);
}


package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.CajaMovimientoMotivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CajaMovimientoMotivoRepository extends JpaRepository<CajaMovimientoMotivo, Long> {

    @Query("SELECT c FROM CajaMovimientoMotivo c WHERE c.active = true  ORDER BY c.movementType ASC, c.name ASC")
    List<CajaMovimientoMotivo> findAllByActiveTrueOrderByMovementTypeNameAsc();
}

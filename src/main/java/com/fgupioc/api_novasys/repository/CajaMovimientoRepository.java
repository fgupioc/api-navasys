package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.CajaMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CajaMovimientoRepository extends JpaRepository<CajaMovimiento, Long> {

    @Query("SELECT cm FROM CajaMovimiento cm WHERE cm.cajaSesion.id = :cajaSesionId and cm.deletedAt IS NULL order by cm.id desc")
    List<CajaMovimiento> findByCajaSesionId(Long cajaSesionId);
}

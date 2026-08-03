package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.CajaSesion;
import com.fgupioc.api_novasys.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CajaSesionRepository extends JpaRepository<CajaSesion, Long> {

    @Query("SELECT c FROM CajaSesion c WHERE c.user.id = :userId AND c.status = 'OPEN' and DATE(c.openedAt) = current_date")
    Optional<CajaSesion> findByCajaOpenByUser(Long userId);
}

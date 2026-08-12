package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 09/08/2026
 */
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    @Query("SELECT t FROM Transferencia t WHERE t.uuid = :uuid AND t.deletedAt IS NULL")
    Optional<Transferencia> findByUUID(UUID uuid);
}

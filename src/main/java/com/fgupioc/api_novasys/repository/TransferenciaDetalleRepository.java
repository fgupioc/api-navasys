package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Transferencia;
import com.fgupioc.api_novasys.models.TransferenciaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferenciaDetalleRepository extends JpaRepository<TransferenciaDetalle, Long> {
    void deleteAllByTransferencia(Transferencia transferencia);
}

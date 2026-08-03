package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CajaRepository extends JpaRepository<Caja, Long> {

    @Query("select c from Caja c where c.almacen = :almacen")
    List<Caja> listaCajasPorAlmacen(Almacen almacen);

    Optional<Caja> findByUuid(UUID uuid);
}

package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    List<Almacen> findAllByTienda(Tienda tienda);

    Optional<Almacen> findByUuid(UUID uuid);
}

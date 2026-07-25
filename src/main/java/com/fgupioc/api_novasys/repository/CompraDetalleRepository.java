package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Compra;
import com.fgupioc.api_novasys.models.CompraDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraDetalleRepository extends JpaRepository<CompraDetalle, Long> {

    @Query("select cd from CompraDetalle cd join fetch cd.producto where cd.compra = :compra")
    List<CompraDetalle> findByCompraWithProducto(@Param("compra") Compra compra);

    void deleteAllByCompra(Compra compra);

    List<CompraDetalle> findByCompra(Compra compraDB);
}

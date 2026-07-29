package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Venta;
import com.fgupioc.api_novasys.models.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {
    @Query("select cd from VentaDetalle cd join fetch cd.producto where cd.venta = :venta")
    List<VentaDetalle> findByVentaWithProducto(@Param("venta") Venta venta);

    void deleteAllByVenta(Venta venta);

    List<VentaDetalle> findByVenta(Venta venta);

    @Query("select v from Venta v where v.uuid = :uuid")
    Optional<Venta> findByUuid(UUID uuid);
}

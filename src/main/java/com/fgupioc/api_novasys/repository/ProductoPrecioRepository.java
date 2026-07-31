package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.Producto;
import com.fgupioc.api_novasys.models.ProductoPrecio;
import com.fgupioc.api_novasys.models.enums.TipoPreciosTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoPrecioRepository extends JpaRepository<ProductoPrecio, Long> {
    Optional<ProductoPrecio> findByProductoAndAlmacenAndPriceType(Producto producto, Almacen almacen, TipoPreciosTipo tipoPreciosTipo);

    List<ProductoPrecio> findAllByProductoAndAlmacen(Producto producto, Almacen almacen);

    @Query("SELECT pp FROM ProductoPrecio pp WHERE pp.producto.id = :productId AND pp.almacen.tienda.id = :tiendaId")
    List<ProductoPrecio> findByProductoId(Long tiendaId, Long productId);

    @Modifying
    @Query("UPDATE ProductoPrecio pp SET pp.salePrice = :salePrice WHERE pp.id = :id")
    void updatePrecio(Long id, BigDecimal salePrice);
}

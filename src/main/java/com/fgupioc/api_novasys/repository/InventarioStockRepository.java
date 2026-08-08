package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Almacen;
import com.fgupioc.api_novasys.models.InventarioStock;
import com.fgupioc.api_novasys.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface InventarioStockRepository extends JpaRepository<InventarioStock, Integer> {
    @Modifying
    @Query("update InventarioStock im set im.quantity = im.quantity - :quantity where im.almacen.id = :warehouseId and im.producto.id = :productId")
    void restarStock(Long warehouseId, Long productId, BigDecimal quantity);

    @Modifying
    @Query("update InventarioStock im set im.quantity = im.quantity + :quantity where im.almacen.id = :warehouseId and im.producto.id = :productId")
    void sumarStock(Long warehouseId, Long productId, BigDecimal quantity);

    @Query("select im from InventarioStock im where im.producto = :producto and im.almacen = :almacen")
    Optional<InventarioStock> findByProductoAndAlmacen(Producto producto, Almacen almacen);

    @Query("select im from InventarioStock im where im.almacen = :almacen and im.producto = :producto")
    Optional<InventarioStock> findByAlmacenAndProducto(Almacen almacen, Producto producto);
}

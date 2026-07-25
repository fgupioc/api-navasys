package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Compra;
import com.fgupioc.api_novasys.models.interfaces.CompraListado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query(value = """
            select BIN_TO_UUID(c.uuid) as uuid,
                   c.document_series,
                   c.document_number,
                   c.purchase_date,
                   c.total,
                   c.cod_status,
                   ms.nombre as status,
                   p.name as supplier
            from compras c
            inner join proveedores p on c.supplier_id = p.id
            inner join maestro ms on ms.tabla_id = 3 and ms.codigo = c.cod_status
            order by c.created_at desc;
            """, nativeQuery = true)
    List<CompraListado> findAllCompras();

    @Query(value = """
            select BIN_TO_UUID(c.uuid) as uuid,
                   c.document_series as documentSeries,
                   c.document_number as documentNumber,
                   c.purchase_date as purchaseDate,
                   c.total as total,
                   c.cod_status as codStatus,
                   ms.nombre as status,
                   p.name as supplier
            from compras c
            inner join proveedores p on c.supplier_id = p.id
            inner join maestro ms on ms.tabla_id = 3 and ms.codigo = c.cod_status
            where (:desde is null or c.purchase_date >= :desde)
              and (:hasta is null or c.purchase_date <= :hasta)
              and (:supplierId is null or c.supplier_id = :supplierId)
              and (:codStatus is null or c.cod_status = :codStatus)
            order by c.created_at desc
            """, nativeQuery = true)
    List<CompraListado> filtrarCompras(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("supplierId") Long supplierId,
            @Param("codStatus") String codStatus
    );

    Optional<Compra> findByUuid(UUID uuid);
}

package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Venta;
import com.fgupioc.api_novasys.models.interfaces.VentaListado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query(value = "SELECT * FROM ventas WHERE deleted_at IS NULL order by id desc", nativeQuery = true)
    List<Venta> findAllByOrderByIdDesc();

    @Query(value = "SELECT * FROM ventas WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Venta> findAllTrashed();

    @Query(value = "SELECT * FROM ventas", nativeQuery = true)
    List<Venta> findAllWithTrashed();

    @Modifying
    @Query(value = "UPDATE ventas SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);

    @Modifying
    @Query(value = "update ventas u set u.deleted_at = null where u.id = :id", nativeQuery = true)
    void resetDeletedAt(Long id);

    Optional<Venta> findByUuid(UUID uuid);

    @Query(value = """
            select
               v.id,
               BIN_TO_UUID(v.uuid) as uuid,
               v.code,
               DATE_FORMAT(v.created_at, '%d/%m/%Y') AS saleDate,
               DATE_FORMAT(v.created_at, '%H:%i') AS saleTime,
               concat(c.first_name, ' ', c.last_name) AS customer,
               c.document_type as documentType,
               c.document_number as documentNumber,
               mp.nombre as paymentMethod,
               mp.valor_string as paymentMethodStyle,
               v.total,
              v.cod_status as codStatus,
               ms.nombre as status,
               ms.valor_string as statusStyle,
               ca.code as cashRegister
                from ventas v
            inner join clientes c on v.customer_id = c.id
            inner join cajas ca on v.caja_id = ca.id
            INNER JOIN maestro ms ON ms.tabla_id = 4 AND ms.codigo = v.cod_status
            left JOIN maestro mp ON mp.tabla_id = 5 AND mp.codigo = v.payment_method
            where (:desde is null or v.sale_date >= :desde)
              and (:hasta is null or v.sale_date <= :hasta)
              and (:customerId is null or v.customer_id = :customerId)
              and (:codStatus is null or v.cod_status = :codStatus)
            and v.deleted_at is null
            order by v.created_at desc
            """, nativeQuery = true)
    List<VentaListado> filtrarVentas(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("customerId") Long customerId,
            @Param("codStatus") String codStatus
    );

    @Query("select count (v) from Venta v where v.documentType = :documentType ")
    Long countByDocumentType(@Param("documentType") String documentType);
}

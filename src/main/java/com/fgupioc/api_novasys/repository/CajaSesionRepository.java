package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.Caja;
import com.fgupioc.api_novasys.models.CajaSesion;
import com.fgupioc.api_novasys.models.interfaces.ICajaCierreList;
import com.fgupioc.api_novasys.models.interfaces.ITicketCierreCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CajaSesionRepository extends JpaRepository<CajaSesion, Long> {

    @Query("SELECT c FROM CajaSesion c WHERE c.user.id = :userId AND c.status = 'OPEN' and DATE(c.openedAt) = current_date")
    Optional<CajaSesion> findByCajaOpenByUser(Long userId);

    Optional<CajaSesion> findByCaja(Caja caja);

    @Query(value = """
                    select cs.id,
                           cs.closed_at              as closedAt,
                           c.name                    as cashRegisterName,
                           u.name                    as userName,
                           u.last_name               as userLastName,
                           cs.opening_amount         as openingAmount,
                           cs.closing_amount         as closingAmount,
                           cs.cash_difference_amount as cashDifferenceAmount,
                           cs.total_payments_amount  as totalPaymentsAmount,
                           cs.status
                    from caja_sesion cs
                             inner join cajas c on c.id = cs.cash_register_id
                             inner join users u on u.id = cs.user_id
                    where cs.user_id = :userId
            and (:desde is null or date(cs.created_at) >= :desde)
            and (:hasta is null or date(cs.created_at) <= :hasta)
            and (:cajaId is null or c.id = :cajaId)
            and (:codStatus is null or cs.status = :codStatus)
            and cs.deleted_at is null
            order by cs.created_at desc
            """, nativeQuery = true)
    List<ICajaCierreList> findAllCajaCierreListByUserId(
            @Param("userId") Long userId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("cajaId") Long cajaId,
            @Param("codStatus") String codStatus
    );

    @Query(value = """
            select cs.created_at as createdAt,
                   c.name                                         as cashRegisterName,
                   c.description                                  as cashRegisterDescription,
                   DATE_FORMAT(cs.opened_at, '%d/%m/%Y %h:%i %p') AS openedAt,
                   DATE_FORMAT(cs.closed_at, '%d/%m/%Y %h:%i %p') AS closedAt,
                   CONCAT(
                           TIMESTAMPDIFF(HOUR, cs.opened_at, cs.closed_at),
                           ' h ',
                           MOD(TIMESTAMPDIFF(MINUTE, cs.opened_at, cs.closed_at), 60),
                           ' min'
                   )                                              AS horas,
                   concat(u.name, ' ', u.last_name)               as userName,
                   u.document                                     as userDocument,
                   r.name                                         as userRole,
                   CASE
                       WHEN TIME(cs.opened_at) >= '06:00:00'
                           AND TIME(cs.opened_at) < '14:00:00'
                           THEN 'Turno Mañana'
            
                       WHEN TIME(cs.opened_at) >= '14:00:00'
                           AND TIME(cs.opened_at) < '22:00:00'
                           THEN 'Turno Tarde'
            
                       ELSE 'Turno Noche'
                       END                                        AS turno,
                   cs.sales_count                                 as salesCount,
                   cs.total_payments_amount                       as totalPaymentsAmount,
                   cs.opening_amount                              as openingAmount,
                   cs.executive_cash_amount                       as executiveCashAmount,
                   cs.cash_in_amount                              as cashInAmount,
                   cs.cash_out_amount                             as cashOutAmount,
                   cs.expected_cash_amount                        as expectedCashAmount,
                   cs.counted_cash_amount                         as countedCashAmount,
                   cs.cash_difference_amount                      as cashDifferenceAmount,
                   cs.notes,
                   DATE_FORMAT(NOW(), '%d/%m/%Y %h:%i %p')        AS reportGeneratedAt
            
            from caja_sesion cs
                     inner join cajas c on c.id = cs.cash_register_id
                     inner join users u on u.id = cs.user_id
                     inner join roles r on r.id = u.role_id
            where cs.id = :cajaSesionId limit 1
            """, nativeQuery = true)
    Optional<ITicketCierreCaja> findTicketCierreCajaById(@Param("cajaSesionId") Long cajaSesionId);
}

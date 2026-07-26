package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.dtos.ClienteRespDTO;
import com.fgupioc.api_novasys.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query(value = "SELECT * FROM clientes WHERE deleted_at IS NULL order by id desc", nativeQuery = true)
    List<Cliente> findAllByOrderByIdDesc();

    @Query(value = "SELECT * FROM clientes WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Cliente> findAllTrashed();

    @Query(value = "SELECT * FROM clientes", nativeQuery = true)
    List<Cliente> findAllWithTrashed();

    @Modifying
    @Query(value = "UPDATE clientes SET deleted_at = NULL WHERE id = :id", nativeQuery = true)
    void restore(@Param("id") Long id);

    @Modifying
    @Query(value = "update clientes u set u.deleted_at = null where u.id = :id", nativeQuery = true)
    void resetDeletedAt(Long id);

    @Query("select c from Cliente c where c.documentNumber = :documentNumber")
    Optional<Cliente> existsByDocumentNumber(String documentNumber);

    @Query("SELECT c FROM Cliente c WHERE c.documentNumber = :documentNumber AND c.id <> :id")
    Optional<Cliente> existsByDocumentNumberDistinct(Long id, String documentNumber);

    @Query(value = """
            select id,
                   document_type,
                   document_number,
                   first_name,
                   last_name
            from clientes
            where upper(trim(concat(document_number, ' ', first_name, ' ', last_name))) like upper(trim(concat('%', :term, '%')))
            order by first_name asc;
            """, nativeQuery = true)
    List<ClienteRespDTO> autocompletado(@Param("term") String term);
}

package com.fgupioc.api_novasys.repository;


import com.fgupioc.api_novasys.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project ticket-sales
 * @since 01/07/2026
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.document = :document and u.codStatus = '1'")
    Optional<User> login(String document);

    Optional<User> findByDocument(String document);

    boolean existsByDocument(String document);

    List<User> findAllByOrderByIdDesc();

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id <> :id AND u.document = :document")
    boolean existsByDocumentDistinctId(Long id, String document);
}

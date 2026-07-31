package com.fgupioc.api_novasys.repository;

import com.fgupioc.api_novasys.models.ProductoImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 22/07/2026
 */
public interface ProductoImagenRepository extends JpaRepository<ProductoImagen, Long> {
}


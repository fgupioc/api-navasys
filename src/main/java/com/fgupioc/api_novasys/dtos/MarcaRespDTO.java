package com.fgupioc.api_novasys.dtos;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */
public record MarcaRespDTO(
        Long id,
        String code,
        String name,
        String codStatus
) {
}
package com.fgupioc.api_novasys.dtos;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 23/07/2026
 */
public record ClienteRespDTO(
        Long id,
        String documentType,
        String documentNumber,
        String firstName,
        String lastName,
        String phone,
        String codStatus
) {
}

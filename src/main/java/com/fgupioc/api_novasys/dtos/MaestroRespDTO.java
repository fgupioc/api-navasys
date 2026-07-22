package com.fgupioc.api_novasys.dtos;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

public record MaestroRespDTO(
        String codigo,
        String nombre,
        String valorString,
        Double valorNumerico
) {
}

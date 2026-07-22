package com.fgupioc.api_novasys.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

public record MarcaReqDTO(
        @NotBlank(message = "El código es obligatorio")
        String code,
        @NotBlank(message = "El nombre es obligatorio")
        String name,
        @NotBlank(message = "El estado es obligatorio")
        String codStatus
) {
}
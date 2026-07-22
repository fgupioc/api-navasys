package com.fgupioc.api_novasys.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 21/07/2026
 */
public record CategoriaReqDTO(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 20)
        String code,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String name,
        Long parentId,
        String codStatus
) {
}
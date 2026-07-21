package com.fgupioc.api_novasys.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaReqDTO {

    @NotBlank(message = "El código es obligatorio")
    private String code;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El estado es obligatorio")
    private String codStatus;
}
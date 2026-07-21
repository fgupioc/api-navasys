package com.fgupioc.api_novasys.dtos;

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
public class MaestroRespDTO {
    private String codigo;
    private String nombre;
    private String valorString;
    private Double valorNumerico;
}

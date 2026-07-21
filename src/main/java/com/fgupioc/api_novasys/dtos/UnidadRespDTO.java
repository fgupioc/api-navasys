package com.fgupioc.api_novasys.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadRespDTO {
    private Long id;
    private String code;
    private String name;
    private String codType;
    private String codStatus;
}

package com.fgupioc.api_novasys.dtos;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 28/07/2026
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VentaPagoReqDTO {
    private String method;
    private BigDecimal amount;
    private String reference;
}

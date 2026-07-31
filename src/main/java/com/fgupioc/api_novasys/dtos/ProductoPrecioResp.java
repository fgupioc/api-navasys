package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.enums.TipoPreciosTipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 30/07/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoPrecioResp {
    private Long id;
    private TipoPreciosTipo priceType;
    private BigDecimal salePrice;
}

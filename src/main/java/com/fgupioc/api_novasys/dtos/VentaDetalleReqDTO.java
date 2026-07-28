package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.Producto;
import com.fgupioc.api_novasys.models.Venta;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 27/07/2026
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VentaDetalleReqDTO {
    private Long productoId;
    private String productName;
    private Double quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal subtotal;

    public BigDecimal getTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).subtract(discount != null ? discount : BigDecimal.ZERO);
    }
}

package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.Cliente;
import com.fgupioc.api_novasys.models.Venta;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class VentaReqDTO {
    private UUID uuid;
    private Long customerId;
    private String documentType;
    private String documentSeries;
    private String documentNumber;
    private String paymentMethod;
    private LocalDate saleDate;
    private String codStatus;
    private String currency;
    private String observation;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal taxApplied;
    private BigDecimal total;
    private List<VentaDetalleReqDTO> productos = new ArrayList<>();

    public Venta toEntity() {
        return Venta.builder()
                .documentType(this.documentType)
                .documentSeries(this.documentSeries)
                .documentNumber(this.documentNumber)
                .paymentMethod(this.paymentMethod)
                .saleDate(this.saleDate)
                .codStatus(this.codStatus)
                .currency(this.currency)
                .observation(this.observation)
                .subtotal(this.subtotal)
                .discount(this.discount)
                .tax(this.tax)
                .taxApplied(this.taxApplied)
                .total(this.total)
                .build();
    }

    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (VentaDetalleReqDTO detalle : productos) {
            total = total.add(detalle.getTotal());
        }
        return total;
    }
}

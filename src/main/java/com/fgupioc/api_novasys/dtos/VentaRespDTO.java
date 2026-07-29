package com.fgupioc.api_novasys.dtos;

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
public class VentaRespDTO {
    private UUID uuid;
    private String code;
    private ClienteRespDTO customer;
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
    private String cashRegister;
    private List<VentaDetalleRespDTO> productos = new ArrayList<>();
    private List<VentaPagoRespDTO> pagos = new ArrayList<>();
}

package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.TicketCierreCajaData;
import com.fgupioc.api_novasys.dtos.TicketDTO;
import com.fgupioc.api_novasys.service.TicketPdfService;
import com.fgupioc.api_novasys.service.impl.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 29/07/2026
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketPdfService ticketPdfService;
    private final VentaService ventaService;

    @GetMapping(value = "/{uuid}/ticket", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> ticket(@PathVariable UUID uuid) {
        TicketDTO dto = ventaService.getItemParTicket(uuid);
        byte[] pdf = ticketPdfService.generar58(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + dto.serieCorrelativo() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping(value = "/cierre-caja", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> preview(@RequestParam(defaultValue = "faltante") String caso) {
        var data =  this.conFaltante();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=cierre-caja.pdf")
                .body(ticketPdfService.generarCierre(data));
    }

    public static TicketCierreCajaData conFaltante() {
        return new TicketCierreCajaData(
                // --- cabecera ---
                "NOVASYS MARKET",
                "Comercial Nova S.A.C.",
                "20123456789",
                "Jr. Amazonas 450 - Chachapoyas",
                "CIE-2026-000123",

                // --- caja ---
                "Caja 01 - Principal",
                "Sucursal Principal",
                "05/08/2026 08:59 AM",
                "05/08/2026 03:44 PM",
                "6 h 45 min",

                // --- responsable ---
                "Milagros Quispe Alvarado",
                "30000001",
                "CAJERO",
                "Turno Mañana",

                // --- ventas ---
                4,
                new BigDecimal("59.50"),
                List.of(
                        new TicketCierreCajaData.MedioPago("Efectivo", new BigDecimal("45.00")),
                        new TicketCierreCajaData.MedioPago("Yape/Plin", new BigDecimal("14.50"))
                ),

                // --- arqueo ---
                new BigDecimal("150.00"),   // fondo inicial
                new BigDecimal("45.00"),    // ventas en efectivo
                new BigDecimal("255.00"),   // ingresos
                new BigDecimal("376.50"),   // egresos (positivo, la plantilla antepone el signo)
                new BigDecimal("73.50"),    // efectivo esperado
                new BigDecimal("0.00"),     // efectivo contado
                new BigDecimal("-73.50"),   // diferencia
                List.of(),                  // sin conteo -> la seccion no se imprime

                "Cajero cerro sin registrar el conteo de denominaciones. Pendiente de regularizar con supervisor.",
                "05/08/2026 03:44 PM"
        );
    }

    /** Variante con arqueo completo: contado = esperado -> caja cuadrada. */
    public static TicketCierreCajaData cuadrada() {
        TicketCierreCajaData base = conFaltante();
        return new TicketCierreCajaData(
                base.nombreComercial(), base.razonSocial(), base.ruc(),
                base.direccionSucursal(), "CIE-2026-000124",
                base.caja(), base.sucursal(), base.fechaApertura(),
                base.fechaCierre(), base.duracion(),
                base.usuarioNombre(), base.usuarioDocumento(), base.usuarioRol(), base.turno(),
                base.cantidadVentas(), base.totalVentas(), base.mediosPago(),
                base.fondoInicial(), base.ventasEfectivo(), base.ingresos(), base.egresos(),
                new BigDecimal("73.50"),
                new BigDecimal("73.50"),
                BigDecimal.ZERO,
                List.of(
                        den("50.00", 1),
                        den("20.00", 1),
                        den("2.00", 1),
                        den("1.00", 1),
                        den("0.50", 1)
                ),
                null,                       // sin observaciones -> la seccion no se imprime
                base.fechaEmision()
        );
    }

    private static TicketCierreCajaData.Denominacion den(String valor, int cantidad) {
        BigDecimal v = new BigDecimal(valor);
        return new TicketCierreCajaData.Denominacion(v, cantidad, v.multiply(BigDecimal.valueOf(cantidad)));
    }
}
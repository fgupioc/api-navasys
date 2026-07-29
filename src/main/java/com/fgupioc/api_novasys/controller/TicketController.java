package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.TicketDTO;
import com.fgupioc.api_novasys.service.TicketPdfService;
import com.fgupioc.api_novasys.service.impl.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        TicketDTO dto = demo();
        byte[] pdf = ticketPdfService.generar(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + dto.serieCorrelativo() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    public static TicketDTO demo() {
        List<TicketDTO.Linea> lineas = List.of(
                new TicketDTO.Linea(1, "ARROZ COSTEÑO EXTRA 5KG",
                        bd("2"), bd("24.50"), bd("49.00")),
                new TicketDTO.Linea(2, "ACEITE PRIMOR 1L",
                        bd("1"), bd("9.90"), bd("9.90")),
                new TicketDTO.Linea(3, "LECHE GLORIA EVAPORADA ENTERA 400G",
                        bd("6"), bd("4.20"), bd("25.20")),
                new TicketDTO.Linea(4, "PAN FRANCES",
                        bd("10"), bd("0.30"), bd("3.00")),
                new TicketDTO.Linea(5, "DETERGENTE BOLIVAR FLORAL 780G",
                        bd("1"), bd("12.50"), bd("12.50")),
                new TicketDTO.Linea(6, "GASEOSA INCA KOLA 1.5L",
                        bd("2"), bd("7.50"), bd("15.00")),
                new TicketDTO.Linea(1, "ARROZ COSTEÑO EXTRA 5KG",
                        bd("2"), bd("24.50"), bd("49.00")),
                new TicketDTO.Linea(2, "ACEITE PRIMOR 1L",
                        bd("1"), bd("9.90"), bd("9.90")),
                new TicketDTO.Linea(3, "LECHE GLORIA EVAPORADA ENTERA 400G",
                        bd("6"), bd("4.20"), bd("25.20")),
                new TicketDTO.Linea(4, "PAN FRANCES",
                        bd("10"), bd("0.30"), bd("3.00")),
                new TicketDTO.Linea(5, "DETERGENTE BOLIVAR FLORAL 780G",
                        bd("1"), bd("12.50"), bd("12.50")),
                new TicketDTO.Linea(6, "GASEOSA INCA KOLA 1.5L",
                        bd("2"), bd("7.50"), bd("15.00")),
                new TicketDTO.Linea(1, "ARROZ COSTEÑO EXTRA 5KG",
                        bd("2"), bd("24.50"), bd("49.00")),
                new TicketDTO.Linea(2, "ACEITE PRIMOR 1L",
                        bd("1"), bd("9.90"), bd("9.90")),
                new TicketDTO.Linea(3, "LECHE GLORIA EVAPORADA ENTERA 400G",
                        bd("6"), bd("4.20"), bd("25.20")),
                new TicketDTO.Linea(4, "PAN FRANCES",
                        bd("10"), bd("0.30"), bd("3.00")),
                new TicketDTO.Linea(5, "DETERGENTE BOLIVAR FLORAL 780G",
                        bd("1"), bd("12.50"), bd("12.50")),
                new TicketDTO.Linea(6, "GASEOSA INCA KOLA 1.5L",
                        bd("2"), bd("7.50"), bd("15.00"))
        );

        return new TicketDTO(
                "MINIMARKET NOVASYS",
                "BOLETA DE VENTA ELECTRONICA",
                "B001-0000234",
                "MICHELLE LIU",
                "DNI 71234567",
                LocalDateTime.of(2026, 7, 29, 18, 42, 15),
                lineas,
                bd("97.12"),    // op. gravada
                bd("17.48"),    // IGV 18%
                bd("114.60"),   // total
                bd("150.00"),   // recibido
                bd("35.40"),    // vuelto
                "EFECTIVO",
                "F. ROJAS"
        );
    }

    /** Venta corta, para verificar la altura mínima del @page. */
    public static TicketDTO demoCorto() {
        List<TicketDTO.Linea> lineas = List.of(
                new TicketDTO.Linea(1, "AGUA SAN LUIS 625ML",
                        bd("1"), bd("1.50"), bd("1.50")),
                new TicketDTO.Linea(2, "GALLETA SODA FIELD",
                        bd("2"), bd("1.20"), bd("2.40"))
        );

        return new TicketDTO(
                "MINIMARKET NOVASYS",
                "TICKET DE VENTA",
                "T001-0000015",
                "VARIOS",
                "-",
                LocalDateTime.now(),
                lineas,
                bd("3.31"), bd("0.59"), bd("3.90"),
                bd("5.00"), bd("1.10"),
                "YAPE",
                "F. ROJAS"
        );
    }

    private static BigDecimal bd(String v) {
        return new BigDecimal(v);
    }
}
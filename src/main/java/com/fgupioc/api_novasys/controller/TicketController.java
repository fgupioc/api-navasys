package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.TicketResp;
import com.fgupioc.api_novasys.service.TicketPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/{uuid}/ticket", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> ticket(@PathVariable UUID uuid) {
        TicketResp dto = ticketPdfService.generar58(uuid);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=" + dto.filename())
                .contentType(MediaType.APPLICATION_PDF)
                .body(dto.pdf());
    }

    @GetMapping(value = "/cierre-caja/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> preview(@PathVariable Long id) {
        TicketResp dto = ticketPdfService.generarCierre(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=" + dto.filename())
                .contentType(MediaType.APPLICATION_PDF)
                .body(dto.pdf());
    }
}
package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.TicketDTO;
import com.fgupioc.api_novasys.helpers.BarcodeUtil;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 29/07/2026
 */
@Service
@RequiredArgsConstructor
public class TicketPdfService {

    private static final float ANCHO_MM       = 80f;
    private static final float ALTO_BASE_MM   = 95f;  // cabecera + totales + pie + barcode
    private static final float ALTO_LINEA_MM  = 7.5f; // fila de 2 renglones
    private static final float ALTO_WRAP_MM   = 3.2f; // renglón extra si la descripción es larga
    private static final int   CHARS_POR_LINEA = 26;  // aprox. para 60% de 72mm a 8pt

    private final TemplateEngine templateEngine;

    public byte[] generar(TicketDTO t) {
        Context ctx = new Context(Locale.of("es", "PE"));
        ctx.setVariable("t", t);
        ctx.setVariable("barcode",
                BarcodeUtil.code128Base64(t.serieCorrelativo(), 600, 100));
        ctx.setVariable("alturaMm", calcularAlturaMm(t));

        String html = templateEngine.process("ticket", ctx);

        // openhtmltopdf exige XHTML bien formado
        Document doc = Jsoup.parse(html, "UTF-8");
        doc.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .charset(StandardCharsets.UTF_8);

        try (var out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.usePdfUaAccessbility(false);
            builder.useFont(
                    () -> getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"),
                    "TicketMono", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.useFont(
                    () -> getClass().getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf"),
                    "TicketMono", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.withW3cDocument(new W3CDom().fromJsoup(doc), "/");
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Error generando el ticket PDF", e);
        }
    }

    private String calcularAlturaMm(TicketDTO t) {
        float alto = ALTO_BASE_MM;
        for (var l : t.lineas()) {
            alto += ALTO_LINEA_MM;
            int extras = Math.max(0, (l.descripcion().length() - 1) / CHARS_POR_LINEA);
            alto += extras * ALTO_WRAP_MM;
        }
        return new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.US))
                .format(Math.max(alto, 120f));
    }
}
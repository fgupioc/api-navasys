package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.TicketCierreCajaData;
import com.fgupioc.api_novasys.dtos.TicketDTO;
import com.fgupioc.api_novasys.helpers.BarcodeUtil;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfBoxRenderer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketPdfService {
    private static final float MM_A_PT = 72f / 25.4f;
    private static final float ANCHO_MM_80 = 80f;
    private static final float ANCHO_MM_58 = 58f;
    private static final float ALTO_TMP = 1200f;  // lienzo: garantiza 1 sola página
    private static final float COLA_MM = 15f;     // papel sobrante antes del corte

    private static final String PAGE_HEIGHT_TOKEN = "__PAGE_HEIGHT__";

    /** Altura generosa usada solo para el pase de medicion. */
    private static final float MEDICION_MM = 2000f;
    /** Altura de respaldo si la medicion falla. */
    private static final float FALLBACK_MM = 300f;
    /** Colchon para que la ultima linea no quede cortada. */
    private static final float MARGEN_INFERIOR_MM = 4f;

    private final TemplateEngine templateEngine;

    /**
     * Punto de entrada: renderiza en un lienzo largo y luego recorta al contenido.
     */
    public byte[] generar58(TicketDTO t) {
        byte[] crudo = render(t, ALTO_TMP, ANCHO_MM_58, "ticket58");
        return recortar(crudo, ANCHO_MM_58);
    }


    /**
     * Punto de entrada: renderiza en un lienzo largo y luego recorta al contenido.
     */
    public byte[] generar80(TicketDTO t) {
        byte[] crudo = render(t, ALTO_TMP, ANCHO_MM_80, "ticket80");
        return recortar(crudo, ANCHO_MM_80);
    }

    public byte[] generarCierre(TicketCierreCajaData data) {
        Context ctx = new Context();
        ctx.setVariable("t", data);
        String html = templateEngine.process("cierreCaja58", ctx);

        float alturaMm = medirAlturaMm(html);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            renderizar(html, alturaMm, out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el ticket de cierre de caja", e);
        }
    }


    /**
     * Renderiza el HTML a PDF con una altura de página dada (en mm).
     */
    private byte[] render(TicketDTO t, float alturaMm, float ANCHO_MM, String template) {
        Context ctx = new Context(Locale.of("es", "PE"));
        ctx.setVariable("anchoMm", fmt(ANCHO_MM));
        ctx.setVariable("t", t);
        ctx.setVariable("barcode",
                BarcodeUtil.code128Base64(t.serieCorrelativo(), 600, 100));
        ctx.setVariable("alturaMm", fmt(alturaMm));

        String html = templateEngine.process(template, ctx);

        // openhtmltopdf exige XHTML bien formado
        Document doc = Jsoup.parse(html, "UTF-8");
        doc.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .charset(StandardCharsets.UTF_8);

        try (var out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.usePdfUaAccessbility(false);
            builder.useFont(fuente("/fonts/JetBrainsMono-Regular.ttf"),
                    "TicketMono", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.useFont(fuente("/fonts/JetBrainsMono-Bold.ttf"),
                    "TicketMono", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.withW3cDocument(new W3CDom().fromJsoup(doc), "/");
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Error generando el ticket PDF", e);
        }
    }

    /**
     * Ajusta el MediaBox/CropBox al alto real del contenido.
     */
    private byte[] recortar(byte[] pdf, float ANCHO_MM) {
        try (PDDocument doc = PDDocument.load(pdf);   // PDFBox 3 -> Loader.loadPDF(pdf)
             var out = new ByteArrayOutputStream()) {
            log.info("alto: {} mm", doc.getPage(0).getMediaBox().getHeight() / (72f/25.4f));
            log.info("PDF crudo: {} pág, alto {} pt",
                    doc.getNumberOfPages(), doc.getPage(0).getMediaBox().getHeight());

            PDPage page = doc.getPage(0);
            Rectangle2D b = new ContentBoundsFinder(page).find();

            float top = page.getMediaBox().getUpperRightY();
            float minY = (b == null) ? top - 200f : (float) b.getMinY();
            float lower = Math.max(0f, minY - COLA_MM * MM_A_PT);

            PDRectangle nueva = new PDRectangle(0, lower, ANCHO_MM * MM_A_PT, top - lower);
            page.setMediaBox(nueva);
            page.setCropBox(nueva);

            log.info("Ticket recortado a {} mm", nueva.getHeight() / MM_A_PT);

            doc.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Error recortando el ticket", e);
        }
    }

    /**
     * Falla ruidosamente si el TTF no está en el classpath.
     */
    private FSSupplier<InputStream> fuente(String ruta) {
        return () -> {
            InputStream is = getClass().getResourceAsStream(ruta);
            if (is == null) {
                throw new IllegalStateException("Fuente no encontrada en classpath: " + ruta);
            }
            return is;
        };
    }

    private String fmt(float v) {
        return new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.US)).format(v);
    }


    /**
     * Primer pase: se maqueta el documento en una pagina muy alta y se lee la altura
     * real del contenido, para emitir un ticket continuo sin papel en blanco.
     */
    private float medirAlturaMm(String html) {
        try {
            PdfRendererBuilder builder = builderBase(html, MEDICION_MM);
            builder.toStream(OutputStream.nullOutputStream());
            try (PdfBoxRenderer renderer = builder.buildPdfRenderer()) {
                renderer.layout();
                float dots = renderer.getRootBox().getHeight();
                float px = dots / (float) renderer.getSharedContext().getDotsPerPixel();
                float mm = px * 25.4f / 96f;
                return mm + MARGEN_INFERIOR_MM;
            }
        } catch (Exception e) {
            log.warn("No se pudo medir la altura del ticket, se usa {} mm", FALLBACK_MM, e);
            return FALLBACK_MM;
        }
    }

    private void renderizar(String html, float alturaMm, OutputStream out) throws Exception {
        PdfRendererBuilder builder = builderBase(html, alturaMm);
        builder.toStream(out);
        builder.run();
    }

    private PdfRendererBuilder builderBase(String html, float alturaMm) {
        String finalHtml = html.replace(PAGE_HEIGHT_TOKEN, String.format(java.util.Locale.ROOT, "%.2f", alturaMm));

        // Jsoup en modo tolerante: openhtmltopdf exige XHTML bien formado.
        Document jsoupDoc = Jsoup.parse(finalHtml);
        jsoupDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withW3cDocument(new W3CDom().fromJsoup(jsoupDoc), "/");

        // Fuente monoespaciada embebida (colocar el .ttf en resources/fonts/).
        builder.useFont(
                () -> getClass().getResourceAsStream("/fonts/JetBrainsMono-Regular.ttf"),
                "TicketMono", 400, PdfRendererBuilder.FontStyle.NORMAL, true);
        builder.useFont(
                () -> getClass().getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf"),
                "TicketMono", 700, PdfRendererBuilder.FontStyle.NORMAL, true);

        return builder;
    }
}
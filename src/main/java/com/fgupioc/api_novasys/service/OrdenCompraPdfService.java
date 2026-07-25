package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.ItemDetalleDTO;
import com.fgupioc.api_novasys.dtos.OrdenCompraPdfDTO;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.Compra;
import com.fgupioc.api_novasys.models.CompraDetalle;
import com.fgupioc.api_novasys.models.Maestro;
import com.fgupioc.api_novasys.repository.CompraDetalleRepository;
import com.fgupioc.api_novasys.repository.CompraRepository;
import com.fgupioc.api_novasys.repository.MaestroRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 25/07/2026
 */
@Service
@RequiredArgsConstructor
public class OrdenCompraPdfService {

    private final TemplateEngine templateEngine;
    private final QrCodeGenerator qrCodeGenerator;
    private final CompraRepository compraRepository;
    private final CompraDetalleRepository compraDetalleRepository;
    private final MaestroRepository maestroRepository;

    public byte[] generar(UUID uuid) throws Exception {
        Compra compra = compraRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada"));

        List<CompraDetalle> detalles = compraDetalleRepository.findByCompraWithProducto(compra);

        List<ItemDetalleDTO> items = new ArrayList<>();
        int i = 1;
        for (CompraDetalle d : detalles) {
            items.add(ItemDetalleDTO.builder()
                    .numero(i++)
                    .codigo(d.getProducto().getCode())
                    .descripcion(d.getProducto().getName())
                    .unidad(d.getProducto().getUnidad().getName())
                    .cantidad(d.getQuantity())
                    .precioUnit(d.getUnitCost())
                    .subtotal(d.getSubtotal())
                    .build());
        }

        BigDecimal subtotal = compra.getSubtotal(); // o suma de items si no lo guardas en cabecera
        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(igv);

        String qr = qrCodeGenerator.generarBase64("https://tuapp.com/compras/" + compra.getUuid(), 150);

        String estadoNombre = maestroRepository
                .findByTablaIdAndCodigo(3, compra.getCodStatus())
                .map(Maestro::getNombre)
                .orElse(compra.getCodStatus());

        OrdenCompraPdfDTO dto = OrdenCompraPdfDTO.builder()
                .empresaNombre("MINIMARKET DEL SUR")
                .empresaDireccion("Av. Los Próceres 1234 - Trujillo, La Libertad")
                .empresaRuc("20123456789")
                .empresaTelefono("044-123456")
                .numeroOrden(compra.getDocumentSeries() + "-" + compra.getDocumentNumber())
                .fecha(compra.getPurchaseDate())
                .proveedorNombre(compra.getProveedor().getName())
                .proveedorRuc(compra.getProveedor().getRuc())
                .proveedorDireccion("Jr. Alfonzo ugarte 160")
                .proveedorTelefono("950016418")
                .codEstado(compra.getCodStatus())
                .estado(estadoNombre)
                .fechaCompra(compra.getPurchaseDate())
                .documentoRef("referencia")
                .almacen(compra.getAlmacen().getName())
                .items(items)
                .observaciones(compra.getObservation())
                .subtotal(subtotal)
                .igv(igv)
                .total(total)
                .elaboradoPor("Franz JUnior")
                .elaboradoRol("Compras")
                .aprobadoPor("Maria Geneveba")
                .aprobadoRol("Gerente")
                .qrBase64(qr)
                .build();

        Context context = new Context();
        context.setVariable("o", dto);

        String html = templateEngine.process("orden-compra", context);

        // Jsoup para tolerancia XML (igual que en lista-pasajeros)
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(Entities.EscapeMode.xhtml);
        String xhtml = jsoupDoc.html();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(xhtml, null);
        builder.toStream(os);
        builder.run();

        return os.toByteArray();
    }
}
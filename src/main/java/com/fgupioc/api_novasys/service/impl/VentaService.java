package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.VentaReqDTO;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.interfaces.VentaListado;
import com.fgupioc.api_novasys.repository.ClienteRepository;
import com.fgupioc.api_novasys.repository.ProductoRepository;
import com.fgupioc.api_novasys.repository.VentaDetalleRepository;
import com.fgupioc.api_novasys.repository.VentaRepository;
import com.fgupioc.api_novasys.service.IVentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 27/07/2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class VentaService implements IVentaService {
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;

    @Override
    public List<VentaListado> filtrarVentas(LocalDate desde, LocalDate hasta, Long customerId, String codStatus) throws RuntimeException {
        return ventaRepository.filtrarVentas(desde, hasta, customerId, codStatus);
    }

    @Override
    @Transactional
    public UUID registrar(VentaReqDTO ventaReqDTO) throws RuntimeException {
        try {
            if (ventaReqDTO.getUuid() != null) {
                return actualizar(ventaReqDTO.getUuid(), ventaReqDTO);
            } else {
                Venta venta = ventaReqDTO.toEntity();
                Cliente cliente = clienteRepository.findById(ventaReqDTO.getCustomerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + ventaReqDTO.getCustomerId()));
                venta.setCustomer(cliente);
                Long count = ventaRepository.count();
                String code = "VEN" + String.format("%06d", count + 1);
                venta.setCode(code);
                BigDecimal total = ventaReqDTO.calcularTotal();
                BigDecimal subtotal = total.divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);
                BigDecimal igv = total.subtract(subtotal);
                venta.setDiscount(new BigDecimal("0"));
                venta.setTax(igv);
                venta.setSubtotal(subtotal);
                venta.setTotal(total);
                venta.setTaxApplied(new BigDecimal("0.18"));

                Venta savedVenta = ventaRepository.save(venta);

                // Guardar los detalles de la venta
                for (var detail : ventaReqDTO.getProductos()) {
                    Producto producto = productoRepository.findById(detail.getProductoId())
                            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
                    VentaDetalle detalle = new VentaDetalle();
                    detalle.setVenta(savedVenta);
                    detalle.setProducto(producto);
                    detalle.setProductName(producto.getName());
                    detalle.setQuantity(detail.getQuantity());
                    detalle.setUnitPrice(detail.getUnitPrice());
                    detalle.setDiscount(detail.getDiscount());
                    detalle.setSubtotal(detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())).subtract(detail.getDiscount()));
                    ventaDetalleRepository.save(detalle);
                }

                log.info("Venta registrada con éxito: {}", savedVenta.getId());
                return savedVenta.getUuid();
            }
        } catch (Exception e) {
            log.error("Error al registrar la venta: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public UUID actualizar(UUID uuid, VentaReqDTO ventaReqDTO) throws RuntimeException {
        try {
            Venta venta = ventaRepository.findByUuid(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con UUID: " + uuid));
            Cliente cliente = clienteRepository.findById(ventaReqDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + ventaReqDTO.getCustomerId()));
            venta.setCustomer(cliente);
            venta.setDocumentType(ventaReqDTO.getDocumentType());
            venta.setDocumentSeries(ventaReqDTO.getDocumentSeries());
            venta.setDocumentNumber(ventaReqDTO.getDocumentNumber());
            venta.setPaymentMethod(ventaReqDTO.getPaymentMethod());
            venta.setSaleDate(ventaReqDTO.getSaleDate());
            venta.setCodStatus(ventaReqDTO.getCodStatus());
            venta.setCurrency(ventaReqDTO.getCurrency());
            venta.setObservation(ventaReqDTO.getObservation());

            BigDecimal total = ventaReqDTO.calcularTotal();
            BigDecimal subtotal = total.divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);
            BigDecimal igv = total.subtract(subtotal);
            venta.setDiscount(new BigDecimal("0"));
            venta.setTax(igv);
            venta.setSubtotal(subtotal);
            venta.setTotal(total);
            venta.setTaxApplied(new BigDecimal("0.18"));

            var savedVenta = ventaRepository.save(venta);
            ventaDetalleRepository.deleteAllByVenta(savedVenta);

            for (var detail : ventaReqDTO.getProductos()) {
                Producto producto = productoRepository.findById(detail.getProductoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
                VentaDetalle detalle = new VentaDetalle();
                detalle.setVenta(savedVenta);
                detalle.setProducto(producto);
                detalle.setProductName(producto.getName());
                detalle.setQuantity(detail.getQuantity());
                detalle.setUnitPrice(detail.getUnitPrice());
                detalle.setDiscount(detail.getDiscount());
                detalle.setSubtotal(detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())).subtract(detail.getDiscount()));
                ventaDetalleRepository.save(detalle);
            }

            log.info("Venta actualizada con éxito: {}", savedVenta.getId());
            return savedVenta.getUuid();
        } catch (Exception e) {
            log.error("Error al actualizar la venta: {}", e.getMessage());
            throw e;
        }
    }
}

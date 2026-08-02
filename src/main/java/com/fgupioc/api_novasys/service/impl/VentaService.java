package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.*;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.interfaces.VentaListado;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.service.IVentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final ModelMapper modelMapper;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final VentaPagoRepository ventaPagoRepository;
    private final MaestroRepository maestroRepository;
    private final AlmacenRepository almacenRepository;
    private final InventarioMovimientoRepository inventarioMovimientoRepository;

    @Override
    public List<VentaListado> filtrarVentas(LocalDate desde, LocalDate hasta, Long customerId, String codStatus) throws RuntimeException {
        return ventaRepository.filtrarVentas(desde, hasta, customerId, codStatus);
    }

    @Override
    @Transactional
    public UUID registrar(VentaReqDTO ventaReqDTO, Authentication authentication) throws RuntimeException {
        try {
            if (ventaReqDTO.getUuid() != null) {
                return actualizar(ventaReqDTO.getUuid(), ventaReqDTO, authentication);
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
                venta.setSaleDate(LocalDate.now());
                if (ventaReqDTO.getCodStatus().equals(Venta.STATUS_COMPLETADA)) {
                    venta.setDocumentSeries("CF01");
                    Long countByDocumentType = ventaRepository.countByDocumentType("12");
                    venta.setDocumentNumber(String.format("%06d", countByDocumentType + 1));
                }

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

                if (ventaReqDTO.getPagos() != null && !ventaReqDTO.getPagos().isEmpty()) {
                    for (var pagoReq : ventaReqDTO.getPagos()) {
                        VentaPago pago = new VentaPago();
                        pago.setSale(savedVenta);
                        pago.setMethod(pagoReq.getMethod());
                        pago.setAmount(pagoReq.getAmount());
                        pago.setReference("");
                        ventaPagoRepository.save(pago);
                    }

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
    public UUID actualizar(UUID uuid, VentaReqDTO ventaReqDTO, Authentication authentication) throws RuntimeException {
        try {

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (principal == null) {
                throw new ResourceNotFoundException("Usuario no autenticado");
            }
            Almacen almacen = almacenRepository.findById(principal.branchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado: " + principal.branchId()));

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
                inventarioMovimientoRepository.restarStock(almacen.getId(), producto.getId(), detail.getQuantity());
            }

            log.info("Venta actualizada con éxito: {}", savedVenta.getId());
            return savedVenta.getUuid();
        } catch (Exception e) {
            log.error("Error al actualizar la venta: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public VentaRespDTO obtenerVentaPorUuid(UUID uuid) throws RuntimeException {
        try {
            Venta ventaDB = ventaDetalleRepository.findByUuid(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
            VentaRespDTO ventaRespDTO = modelMapper.map(ventaDB, VentaRespDTO.class);

            List<VentaDetalle> detalles = ventaDetalleRepository.findByVenta(ventaDB);

            ventaRespDTO.setProductos(detalles.stream()
                    .map(detalle -> {
                        var prod = modelMapper.map(detalle, VentaDetalleRespDTO.class);
                        prod.setProductoId(detalle.getProducto().getId());
                        return prod;
                    })
                    .collect(Collectors.toList()));

            ventaDB.getPayments().forEach(pago -> {
                ventaRespDTO.getPagos().add(modelMapper.map(pago, VentaPagoRespDTO.class));
            });
            if (ventaDB.getCustomer() != null) {
                ventaRespDTO.setCustomer(modelMapper.map(ventaDB.getCustomer(), ClienteRespDTO.class));
            }

            return ventaRespDTO;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void cancelar(UUID uuid) throws RuntimeException {
        try {
            Venta venta = ventaRepository.findByUuid(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con UUID: " + uuid));
            venta.setCodStatus(Venta.STATUS_ANULADA);
            ventaRepository.save(venta);
            log.info("Venta cancelada con éxito: {}", venta.getId());
        } catch (RuntimeException e) {
            log.error("Error al cancelar la venta: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void eliminar(UUID uuid) throws RuntimeException {
        try {
            Venta venta = ventaRepository.findByUuid(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con UUID: " + uuid));
            ventaRepository.delete(venta);
            log.info("Venta eliminada con éxito: {}", venta.getId());
        } catch (RuntimeException e) {
            log.error("Error al eliminar la venta: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public TicketDTO getItemParTicket(UUID uuid) throws RuntimeException {
        Venta venta = ventaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con UUID: " + uuid));
        List<VentaDetalle> detalles = ventaDetalleRepository.findByVenta(venta);

        List<TicketDTO.Linea> lineas = new java.util.ArrayList<>();

        for (int i = 0; i < detalles.size(); i++) {
            VentaDetalle detalle = detalles.get(i);
            lineas.add(new TicketDTO.Linea(
                    i + 1,
                    detalle.getProductName(),
                    bd(detalle.getQuantity().toString()),
                    detalle.getUnitPrice(),
                    bd((detalle.getQuantity() * detalle.getUnitPrice().doubleValue()) + "")
            ));
        }

        Cliente cliente = venta.getCustomer();
        String clienteNombre = "";
        String clienteDocumento = "";
        if (venta.getCustomer() != null) {
            clienteNombre = cliente.getFirstName() + " " + cliente.getLastName();
            clienteDocumento = cliente.getDocumentType() + " " + cliente.getDocumentNumber();
            if (cliente.getId() == 1l) {
                clienteDocumento = "";
            }
        }

        String metodoPago = maestroRepository
                .findByTablaIdAndCodigo(5, venta.getPaymentMethod())
                .map(Maestro::getNombre)
                .orElse("");

        return new TicketDTO(
                "MINIMARKET NOVASYS",
                "TICKET DE VENTA",
                venta.getDocumentSeries() + "-" + venta.getDocumentNumber(),
                venta.getCode(),
                clienteNombre,
                clienteDocumento,
                venta.getCreatedAt(),
                lineas,
                venta.getSubtotal(), venta.getTax(), venta.getTotal(),
                venta.getTotal(), bd("0.00"),
                metodoPago,
                venta.getCashRegister()
        );
    }

    private static BigDecimal bd(String v) {
        return new BigDecimal(v);
    }
}

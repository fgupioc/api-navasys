package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.CompraProducto;
import com.fgupioc.api_novasys.dtos.CompraReqDTO;
import com.fgupioc.api_novasys.dtos.CompraRespDTO;
import com.fgupioc.api_novasys.dtos.mappers.CompraMapper;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.enums.InventarioMovimientoTipo;
import com.fgupioc.api_novasys.models.enums.InventarioReferenciaTipo;
import com.fgupioc.api_novasys.models.enums.TipoPreciosTipo;
import com.fgupioc.api_novasys.models.interfaces.CompraListado;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.service.ICompraService;
import com.fgupioc.api_novasys.service.OrdenCompraPdfService;
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
 * @since 24/07/2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CompraService implements ICompraService {
    private final CompraMapper compraMapper;
    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final AlmacenRepository almacenRepository;
    private final CompraDetalleRepository compraDetalleRepository;
    private final InventarioMovimientoRepository inventarioMovimientoRepository;
    private final ProductoRepository productoRepository;
    private final ProductoPrecioRepository productoPrecioRepository;
    private final OrdenCompraPdfService ordenCompraPdfService;

    @Override
    @Transactional
    public CompraRespDTO registrarCompra(CompraReqDTO compraRespDTO) throws RuntimeException {
        try {
            Long count = compraRepository.count();
            String code = "COM" + String.format("%06d", count + 1);
            Compra compra = compraMapper.toRespEntity(compraRespDTO);
            Proveedor proveedor = proveedorRepository.findById(compraRespDTO.supplierId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            Almacen almacen = almacenRepository.findById(compraRespDTO.warehouseId())
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            BigDecimal total = compraRespDTO.calcularTotal();
            BigDecimal subtotal = total.divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);
            BigDecimal igv = total.subtract(subtotal);

            compra.setCode(code);
            compra.setAlmacen(almacen);
            compra.setProveedor(proveedor);
            compra.setDiscount(new BigDecimal("0"));
            compra.setTax(igv);
            compra.setSubtotal(subtotal);
            compra.setTotal(total);
            compra.setTaxApplied(new BigDecimal("0.18"));

            Compra save = compraRepository.save(compra);

            for (CompraProducto detail : compraRespDTO.products()) {

                CompraDetalle purchaseDetail = new CompraDetalle();

                Producto producto = productoRepository.findById(detail.id())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                purchaseDetail.setCompra(compra);
                purchaseDetail.setProducto(producto);
                purchaseDetail.setQuantity(detail.quantity());
                purchaseDetail.setUnitCost(detail.unitCost());
                purchaseDetail.setSalePrice(detail.salePrice());
                purchaseDetail.setDiscount(detail.discount());
                purchaseDetail.setSubtotal(detail.unitCost().multiply(BigDecimal.valueOf(detail.quantity())).subtract(detail.discount()));

                compraDetalleRepository.save(purchaseDetail);

            }
            return compraMapper.toRespDTO(save, List.of());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public CompraRespDTO actualizarCompra(UUID uuid, CompraReqDTO compraRespDTO) throws RuntimeException {
        try {
            Compra compraDB = compraRepository.findByUuid(uuid)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            Proveedor proveedor = proveedorRepository.findById(compraRespDTO.supplierId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            Almacen almacen = almacenRepository.findById(compraRespDTO.warehouseId())
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

            BigDecimal total = compraRespDTO.calcularTotal();
            BigDecimal subtotal = total.divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);
            BigDecimal igv = total.subtract(subtotal);

            compraDB.setProveedor(proveedor);
            compraDB.setAlmacen(almacen);
            compraDB.setPurchaseDate(compraRespDTO.purchaseDate());
            compraDB.setDocumentSeries(compraRespDTO.documentSeries());
            compraDB.setDocumentNumber(compraRespDTO.documentNumber());
            compraDB.setDocumentType(compraRespDTO.documentType());
            compraDB.setCurrency(compraRespDTO.currency());
            compraDB.setPaymentType(compraRespDTO.paymentType());
            compraDB.setObservation(compraRespDTO.observation());

            compraDB.setDiscount(new BigDecimal("0"));
            compraDB.setTax(igv);
            compraDB.setSubtotal(subtotal);
            compraDB.setTotal(total);
            compraDB.setTaxApplied(new BigDecimal("0.18"));
            compraDB.setCodStatus(compraRespDTO.codStatus());

            Compra save = compraRepository.save(compraDB);

            List<CompraDetalle> detalles = compraDetalleRepository.findByCompra(compraDB);
            if (compraRespDTO.codStatus().equalsIgnoreCase("5")) {
                for (CompraDetalle detalle : detalles) {
                    inventarioMovimientoRepository.deleteAllByReferenceId(detalle.getId());
                }
            }

            compraDetalleRepository.deleteAll(detalles);
            for (CompraProducto detail : compraRespDTO.products()) {
                CompraDetalle purchaseDetail = new CompraDetalle();

                Producto producto = productoRepository.findById(detail.id())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                purchaseDetail.setCompra(compraDB);
                purchaseDetail.setProducto(producto);
                purchaseDetail.setQuantity(detail.quantity());
                purchaseDetail.setUnitCost(detail.unitCost());
                purchaseDetail.setSalePrice(detail.salePrice());
                purchaseDetail.setDiscount(detail.discount());
                purchaseDetail.setSubtotal(detail.unitCost().multiply(BigDecimal.valueOf(detail.quantity())).subtract(detail.discount()));

                compraDetalleRepository.save(purchaseDetail);

                if (compraRespDTO.codStatus().equalsIgnoreCase("5")) {
                    InventarioMovimiento movement = new InventarioMovimiento();

                    movement.setProducto(producto);
                    movement.setAlmacen(compraDB.getAlmacen());
                    movement.setTipoMovimiento(InventarioMovimientoTipo.PURCHASE_IN);
                    movement.setTipoReferencia(InventarioReferenciaTipo.PURCHASE);
                    movement.setReferenceId(purchaseDetail.getId());
                    movement.setInitialQuantity(detail.quantity());
                    movement.setCurrentQuantity(detail.quantity());
                    movement.setUnitCost(detail.unitCost());
                    movement.setSalePrice(detail.salePrice());
                    movement.setDiscount(detail.discount());

                    inventarioMovimientoRepository.save(movement);
                    List<ProductoPrecio> productoPrecioDB = productoPrecioRepository.findAllByProductoAndAlmacen(producto, almacen);
                    List<TipoPreciosTipo> tiposPrecios = List.of(TipoPreciosTipo.REGULAR, TipoPreciosTipo.WHOLESALE, TipoPreciosTipo.SALE);
                    for (TipoPreciosTipo tipoPrecio : tiposPrecios) {
                        ProductoPrecio productoPrecio = productoPrecioDB.stream()
                                .filter(pp -> pp.getPriceType() == tipoPrecio)
                                .findFirst()
                                .orElseGet(() -> {
                                    ProductoPrecio newProductoPrecio = new ProductoPrecio();
                                    newProductoPrecio.setProducto(producto);
                                    newProductoPrecio.setAlmacen(almacen);
                                    newProductoPrecio.setPriceType(tipoPrecio);
                                    return newProductoPrecio;
                                });
                        productoPrecio.setSalePrice(detail.salePrice());
                        productoPrecioRepository.save(productoPrecio);
                    }

                }
            }
            return compraMapper.toRespDTO(save, List.of());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CompraListado> listarCompras() throws RuntimeException {
        return compraRepository.findAllCompras();
    }

    @Override
    public List<CompraListado> filtrarCompras(LocalDate desde, LocalDate hasta, Long supplierId, String codStatus) throws RuntimeException {
        return compraRepository.filtrarCompras(desde, hasta, supplierId, codStatus);
    }

    @Override
    public CompraRespDTO buscarCompra(UUID uuid) throws RuntimeException {
        Compra compra = compraRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        log.info(compra.toString());
        List<CompraDetalle> detalles = compraDetalleRepository.findByCompraWithProducto(compra);
        List<CompraProducto> list = detalles.stream()
                .map(CompraProducto::toDetalleDTO)
                .toList();
        return compraMapper.toRespDTO(compra, list);
    }

    @Override
    public byte[] generar(UUID uuid) throws Exception {
        return ordenCompraPdfService.generar(uuid);
    }

    @Override
    public void anularCompra(UUID uuid) throws RuntimeException {
        Compra compra = compraRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        compra.setCodStatus("6");
        compraRepository.save(compra);
    }
}

package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.CompraProducto;
import com.fgupioc.api_novasys.dtos.CompraReqDTO;
import com.fgupioc.api_novasys.dtos.CompraRespDTO;
import com.fgupioc.api_novasys.dtos.mappers.CompraMapper;
import com.fgupioc.api_novasys.helpers.CONST;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private final InventarioStockRepository inventarioStockRepository;
    private final UserRepository userRepository;
    private final OrdenCompraPdfService ordenCompraPdfService;

    @Override
    @Transactional
    public CompraRespDTO registrarCompra(CompraReqDTO compraRespDTO, Authentication authentication) throws RuntimeException {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (principal == null) {
                throw new ResourceNotFoundException("Usuario no autenticado");
            }
            User user = userRepository.findById(principal.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + principal.id()));

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
            compra.setUser(user);
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
                purchaseDetail.setSubtotal(detail.unitCost().multiply(detail.quantity()).subtract(detail.discount()));

                compraDetalleRepository.save(purchaseDetail);

            }
            return compraMapper.toRespDTO(save, List.of());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public CompraRespDTO actualizarCompra(UUID uuid, CompraReqDTO compraRespDTO, Authentication authentication) throws RuntimeException {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (principal == null) {
                throw new ResourceNotFoundException("Usuario no autenticado");
            }
            User user = userRepository.findById(principal.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + principal.id()));

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
            compraDB.setUser(user);
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

            List<CompraDetalle> detalles = compraDetalleRepository.findByCompra(compraDB);
            if (compraRespDTO.codStatus().equalsIgnoreCase("5")) {
                compraDetalleRepository.deleteAll(detalles);
            }

            Compra save = compraRepository.save(compraDB);

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
                purchaseDetail.setSubtotal(detail.unitCost().multiply(detail.quantity()).subtract(detail.discount()));

                compraDetalleRepository.save(purchaseDetail);

                if (compraRespDTO.codStatus().equalsIgnoreCase("5")) {
                    Optional<InventarioMovimiento> exist = inventarioMovimientoRepository.findLastByProductoAndAlmacen(producto, compraDB.getAlmacen());
                    InventarioMovimiento movement =  new InventarioMovimiento();
                    movement.setId(null);
                    movement.setUser(user);
                    movement.setProducto(producto);
                    movement.setAlmacen(compraDB.getAlmacen());
                    movement.setTipoMovimiento(InventarioMovimientoTipo.PURCHASE_IN);
                    movement.setTipoReferencia(InventarioReferenciaTipo.PURCHASE);
                    movement.setReferenceId(save.getId());
                    if (exist.isPresent()) {
                        movement.setStockBefore(exist.get().getStockAfter());
                        movement.setStockAfter(detail.quantity().add(exist.get().getStockAfter()));
                    } else {
                        movement.setStockBefore(BigDecimal.ZERO);
                        movement.setStockAfter(detail.quantity());
                    }
                    movement.setQuantity(detail.quantity());
                    movement.setUnitCost(detail.unitCost());
                    movement.setSalePrice(detail.salePrice());

                    inventarioMovimientoRepository.save(movement);
                    InventarioStock stock = new InventarioStock();
                    stock.setId(null);
                    stock.setQuantity(BigDecimal.ZERO);
                    stock.setLastCost(BigDecimal.ZERO);

                    InventarioStock inventarioStock = inventarioStockRepository.findByProductoAndAlmacen(producto, almacen).orElse(stock);

                    inventarioStock.setProducto(producto);
                    inventarioStock.setAlmacen(almacen);
                    inventarioStock.setAverageCost(calcularCostoPromedio(inventarioStock.getQuantity(), inventarioStock.getLastCost(), detail.quantity(), detail.unitCost()));
                    inventarioStock.setQuantity(inventarioStock.getQuantity().add(detail.quantity()));
                    inventarioStock.setLastCost(inventarioStock.getLastCost());
                    inventarioStock.setCodStatus(CONST.STATUS_ACTIVE);
                    inventarioStock.setInitialQuantity(movement.getStockBefore());
                    inventarioStockRepository.save(inventarioStock);

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
                                    if (tipoPrecio.equals(TipoPreciosTipo.SALE)) {
                                        newProductoPrecio.setCodStatus(CONST.STATUS_ACTIVE);
                                    } else {
                                        newProductoPrecio.setCodStatus(CONST.STATUS_INACTIVE);
                                    }
                                    newProductoPrecio.setMinimumQuantity(1);
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

    private BigDecimal calcularCostoPromedio(
            BigDecimal stockActual,
            BigDecimal costoActual,
            BigDecimal cantidadEntrada,
            BigDecimal costoEntrada) {

        if (stockActual == null) {
            stockActual = BigDecimal.ZERO;
        }

        if (costoActual == null) {
            costoActual = BigDecimal.ZERO;
        }

        if (cantidadEntrada == null || cantidadEntrada.compareTo(BigDecimal.ZERO) <= 0) {
            return costoActual;
        }

        if (costoEntrada == null) {
            costoEntrada = BigDecimal.ZERO;
        }

        BigDecimal valorStockActual =
                stockActual.multiply(costoActual);

        BigDecimal valorNuevaCompra =
                cantidadEntrada.multiply(costoEntrada);

        BigDecimal valorTotal =
                valorStockActual.add(valorNuevaCompra);

        BigDecimal cantidadTotal =
                stockActual.add(cantidadEntrada);

        return valorTotal.divide(
                cantidadTotal,
                4,
                RoundingMode.HALF_UP
        );
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

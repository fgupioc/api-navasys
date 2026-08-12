package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.TransferenciaDetalleReqDTO;
import com.fgupioc.api_novasys.dtos.TransferenciaReqDTO;
import com.fgupioc.api_novasys.helpers.CONST;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.enums.EstadoTransferencia;
import com.fgupioc.api_novasys.models.enums.InventarioMovimientoTipo;
import com.fgupioc.api_novasys.models.enums.InventarioReferenciaTipo;
import com.fgupioc.api_novasys.models.enums.TipoPreciosTipo;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.service.ITransferenciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 09/08/2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransferenciaService implements ITransferenciaService {
    private final UserRepository userRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final TransferenciaDetalleRepository transferenciaDetalleRepository;
    private final InventarioMovimientoRepository inventarioMovimientoRepository;
    private final ProductoPrecioRepository productoPrecioRepository;
    private final InventarioStockRepository inventarioStockRepository;

    private User getUser(Authentication authentication) throws ResourceNotFoundException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (principal == null) {
            throw new ResourceNotFoundException("Usuario no autenticado");
        }

        return userRepository.findById(principal.id())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + principal.id()));
    }

    @Override
    @Transactional
    public UUID registrarTransferencia(TransferenciaReqDTO transferenciaReqDTO, Authentication authentication) throws RuntimeException {
        try {
            if (transferenciaReqDTO.getUuid() != null) {
                actualizarTransferencia(transferenciaReqDTO.getUuid(), transferenciaReqDTO, authentication);
            }

            Almacen almacenOrigen = almacenRepository.findById(transferenciaReqDTO.getAlmacenOrigenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado: " + transferenciaReqDTO.getAlmacenOrigenId()));

            Almacen almacenDestino = almacenRepository.findById(transferenciaReqDTO.getAlmacenDestinoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado: " + transferenciaReqDTO.getAlmacenDestinoId()));
            Long count = transferenciaRepository.count();
            String code = "VEN" + String.format("%06d", count + 1);
            Transferencia transferencia = new Transferencia();
            transferencia.setId(null);
            transferencia.setUser(getUser(authentication));
            transferencia.setSourceWarehouse(almacenOrigen);
            transferencia.setDestinationWarehouse(almacenDestino);
            transferencia.setCode(code);
            transferencia.setFechaTransferencia(LocalDateTime.now());
            transferencia.setCodStatus(EstadoTransferencia.PENDING);
            transferencia.setObservation(transferenciaReqDTO.getObservation());
            Transferencia transferenciaSave  = transferenciaRepository.save(transferencia);

            for (TransferenciaDetalleReqDTO detail : transferenciaReqDTO.getProductos()) {
                Producto producto = productoRepository.findById(detail.getProductoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
                TransferenciaDetalle detalle =  new TransferenciaDetalle();
                detalle.setId(null);
                detalle.setTransferencia(transferenciaSave);
                detalle.setProduct(producto);
                detalle.setQuantity(detail.getQuantity());
                detalle.setQuantityReceived(BigDecimal.ZERO);

                transferenciaDetalleRepository.save(detalle);
            }

            return transferenciaSave.getUuid();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public UUID actualizarTransferencia(UUID uuid, TransferenciaReqDTO transferenciaReqDTO, Authentication authentication) throws RuntimeException {
        try {
            Transferencia transferencia = transferenciaRepository.findByUUID(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Transferencia no encontrada con UUID: " + uuid.toString()));

            Almacen almacenOrigen = almacenRepository.findById(transferenciaReqDTO.getAlmacenOrigenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado: " + transferenciaReqDTO.getAlmacenOrigenId()));

            Almacen almacenDestino = almacenRepository.findById(transferenciaReqDTO.getAlmacenDestinoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado: " + transferenciaReqDTO.getAlmacenDestinoId()));

            User user = getUser(authentication);

            transferencia.setSourceWarehouse(almacenOrigen);
            transferencia.setDestinationWarehouse(almacenDestino);
            transferencia.setCodStatus(transferenciaReqDTO.getCodStatus());
            transferencia.setObservation(transferenciaReqDTO.getObservation());
            transferenciaRepository.save(transferencia);

            transferenciaDetalleRepository.deleteAllByTransferencia(transferencia);

            for (TransferenciaDetalleReqDTO detail : transferenciaReqDTO.getProductos()) {
                Producto producto = productoRepository.findById(detail.getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
                TransferenciaDetalle detalle =  new TransferenciaDetalle();
                detalle.setId(null);
                detalle.setTransferencia(transferencia);
                detalle.setProduct(producto);
                detalle.setQuantity(detail.getQuantity());
                detalle.setQuantityReceived(BigDecimal.ZERO);

                transferenciaDetalleRepository.save(detalle);

                if (transferenciaReqDTO.getCodStatus().equals(EstadoTransferencia.COMPLETED)) {
                    List<ProductoPrecio> productoPrecioDB = productoPrecioRepository.findAllByProductoAndAlmacen(producto, almacenOrigen);
                    if (productoPrecioDB.isEmpty()) {
                        throw new ResourceNotFoundException("No se encontró el precio del producto en el almacén de origen");
                    }
                    ProductoPrecio precioVenta = productoPrecioDB.stream().filter(pp -> pp.getPriceType().equals(TipoPreciosTipo.SALE)).findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el precio de venta del producto en el almacén de origen"));
                    if (precioVenta.getSalePrice() == null) {
                        throw new ResourceNotFoundException("El precio de venta del producto en el almacén de origen es nulo");
                    }

                    ProductoPrecio precioOferta = productoPrecioDB.stream().filter(pp -> pp.getPriceType().equals(TipoPreciosTipo.REGULAR)).findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el precio regular del producto en el almacén de origen"));
                    if (precioOferta.getSalePrice() == null) {
                      precioOferta.setSalePrice(precioVenta.getSalePrice());
                    }

                    ProductoPrecio precioMayorista = productoPrecioDB.stream().filter(pp -> pp.getPriceType().equals(TipoPreciosTipo.WHOLESALE)).findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("No se encontró el precio mayorista del producto en el almacén de origen"));
                    if (precioMayorista.getSalePrice() == null) {
                        precioMayorista.setSalePrice(precioVenta.getSalePrice());
                    }

                    Optional<InventarioMovimiento> exist = inventarioMovimientoRepository.findLastByProductoAndAlmacen(producto, almacenDestino);
                    InventarioMovimiento movement =  new InventarioMovimiento();
                    movement.setId(null);
                    movement.setUser(user);
                    movement.setProducto(producto);
                    movement.setAlmacen(almacenDestino);
                    movement.setTipoMovimiento(InventarioMovimientoTipo.TRANSFER_IN);
                    movement.setTipoReferencia(InventarioReferenciaTipo.TRANSFER);
                    movement.setReferenceId(transferencia.getId());
                    if (exist.isPresent()) {
                        movement.setStockBefore(exist.get().getStockAfter());
                        movement.setStockAfter(detail.getQuantity().add(exist.get().getStockAfter()));
                    } else {
                        movement.setStockBefore(BigDecimal.ZERO);
                        movement.setStockAfter(detail.getQuantity());
                    }
                    movement.setQuantity(detail.getQuantity());
                    movement.setUnitCost(precioVenta.getSalePrice());
                    movement.setSalePrice(precioVenta.getSalePrice());

                    inventarioMovimientoRepository.save(movement);
                    InventarioStock stock = new InventarioStock();
                    stock.setId(null);
                    stock.setQuantity(BigDecimal.ZERO);
                    stock.setLastCost(BigDecimal.ZERO);

                    InventarioStock inventarioStock = inventarioStockRepository.findByProductoAndAlmacen(producto, almacenDestino).orElse(stock);

                    inventarioStock.setProducto(producto);
                    inventarioStock.setAlmacen(almacenDestino);
                    inventarioStock.setAverageCost(calcularCostoPromedio(inventarioStock.getQuantity(), inventarioStock.getLastCost(), detail.getQuantity(), precioVenta.getSalePrice()));
                    inventarioStock.setQuantity(inventarioStock.getQuantity().add(detail.getQuantity()));
                    inventarioStock.setLastCost(inventarioStock.getLastCost());
                    inventarioStock.setCodStatus(CONST.STATUS_ACTIVE);
                    inventarioStock.setInitialQuantity(movement.getStockBefore());
                    inventarioStockRepository.save(inventarioStock);

                    List<ProductoPrecio> productoPrecioDB2 = productoPrecioRepository.findAllByProductoAndAlmacen(producto, almacenDestino);
                    for (ProductoPrecio productoPrecio : productoPrecioDB) {
                        ProductoPrecio price = productoPrecioDB2.stream()
                                .filter(pp -> pp.getPriceType() == productoPrecio.getPriceType())
                                .findFirst()
                                .orElseGet(() -> {
                                    ProductoPrecio newProductoPrecio = new ProductoPrecio();
                                    newProductoPrecio.setProducto(producto);
                                    newProductoPrecio.setAlmacen(almacenDestino);
                                    if (productoPrecio.getPriceType().equals(TipoPreciosTipo.SALE)) {
                                        newProductoPrecio.setCodStatus(CONST.STATUS_ACTIVE);
                                        newProductoPrecio.setSalePrice(precioVenta.getSalePrice());
                                    } else {
                                        newProductoPrecio.setCodStatus(CONST.STATUS_INACTIVE);
                                    }
                                    if (productoPrecio.getPriceType().equals(TipoPreciosTipo.REGULAR)) {
                                        newProductoPrecio.setSalePrice(precioOferta.getSalePrice());
                                    }
                                    if (productoPrecio.getPriceType().equals(TipoPreciosTipo.WHOLESALE)) {
                                        newProductoPrecio.setSalePrice(precioMayorista.getSalePrice());
                                    }
                                    newProductoPrecio.setMinimumQuantity(1);
                                    return newProductoPrecio;
                                });
                        productoPrecioRepository.save(price);
                    }


                    Optional<InventarioMovimiento> existOrigen = inventarioMovimientoRepository.findLastByProductoAndAlmacen(producto, almacenOrigen);
                    InventarioMovimiento movementOrigen =  new InventarioMovimiento();
                    movementOrigen.setId(null);
                    movementOrigen.setUser(user);
                    movementOrigen.setProducto(producto);
                    movementOrigen.setAlmacen(almacenOrigen);
                    movementOrigen.setTipoMovimiento(InventarioMovimientoTipo.TRANSFER_OUT);
                    movementOrigen.setTipoReferencia(InventarioReferenciaTipo.TRANSFER);
                    movementOrigen.setReferenceId(transferencia.getId());
                    if (existOrigen.isPresent()) {
                        movementOrigen.setStockBefore(existOrigen.get().getStockAfter());
                        movementOrigen.setStockAfter(existOrigen.get().getStockAfter().subtract(detail.getQuantity()));
                    } else {
                        movementOrigen.setStockBefore(BigDecimal.ZERO);
                        movementOrigen.setStockAfter(detail.getQuantity());
                    }
                    movementOrigen.setQuantity(detail.getQuantity());
                    movementOrigen.setUnitCost(precioVenta.getSalePrice());
                    movementOrigen.setSalePrice(precioVenta.getSalePrice());

                    inventarioMovimientoRepository.save(movementOrigen);

                    Optional<InventarioStock> inventarioStockOrigen = inventarioStockRepository.findByProductoAndAlmacen(producto, almacenOrigen);

                    if (inventarioStockOrigen.isPresent()) {
                        InventarioStock inventarioStockOr = inventarioStockOrigen.get();
                        inventarioStockOr.setAverageCost(calcularCostoPromedio(inventarioStockOr.getQuantity(), inventarioStockOr.getLastCost(), detail.getQuantity(), precioVenta.getSalePrice()));
                        inventarioStockOr.setLastCost(inventarioStockOr.getQuantity());
                        inventarioStockOr.setQuantity(inventarioStockOr.getQuantity().subtract(detail.getQuantity()));
                        inventarioStockOr.setCodStatus(CONST.STATUS_ACTIVE);
                        inventarioStockOr.setInitialQuantity(movement.getStockBefore());
                        inventarioStockRepository.save(inventarioStockOr);
                    }

                }
            }

            return transferencia.getUuid();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
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
}

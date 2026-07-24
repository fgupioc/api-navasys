package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.CompraProducto;
import com.fgupioc.api_novasys.dtos.CompraReqDTO;
import com.fgupioc.api_novasys.dtos.CompraRespDTO;
import com.fgupioc.api_novasys.dtos.mappers.CompraMapper;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.enums.InventarioMovimientoTipo;
import com.fgupioc.api_novasys.models.enums.InventarioReferenciaTipo;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.service.ICompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
                    .orElseThrow(() -> new RuntimeException("Almacen no encontrado"));
            compra.setCode(code);
            compra.setAlmacen(almacen);
            compra.setProveedor(proveedor);
            compra.setDiscount(new BigDecimal("0"));
            compra.setTax(new BigDecimal("0.18"));
            compra.setSubtotal(compraRespDTO.calcularTotal());
            compra.setTotal(compraRespDTO.calcularTotal().add(compra.getSubtotal().multiply(compra.getTax())).subtract(compra.getDiscount()));

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

                InventarioMovimiento movement = new InventarioMovimiento();

                movement.setProducto(producto);
                movement.setAlmacen(compra.getAlmacen());
                movement.setTipoMovimiento(InventarioMovimientoTipo.PURCHASE_IN);
                movement.setTipoReferencia(InventarioReferenciaTipo.PURCHASE);;
                movement.setReferenceId(purchaseDetail.getId());
                movement.setQuantity(detail.quantity());
                movement.setUnitCost(detail.unitCost());
                movement.setDiscount(detail.discount());

                inventarioMovimientoRepository.save(movement);

            }
            return compraMapper.toRespDTO(save);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

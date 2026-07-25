package com.fgupioc.api_novasys.dtos.mappers;

import com.fgupioc.api_novasys.dtos.*;
import com.fgupioc.api_novasys.models.Compra;
import com.fgupioc.api_novasys.models.CompraDetalle;
import com.fgupioc.api_novasys.models.Producto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 24/07/2026
 */
@Component
public class CompraMapper {

    public Compra toRespEntity(CompraReqDTO compraReqDTO) {
        if (compraReqDTO == null) {
            return null;
        }

        Compra compra = new Compra();
        compra.setCode(compraReqDTO.code());
        compra.setPurchaseDate(compraReqDTO.purchaseDate());
        compra.setDocumentType(compraReqDTO.documentType());
        compra.setDocumentSeries(compraReqDTO.documentSeries());
        compra.setDocumentNumber(compraReqDTO.documentNumber());
        compra.setPaymentType(compraReqDTO.paymentType());
        compra.setCurrency(compraReqDTO.currency());
        compra.setObservation(compraReqDTO.observation());
        compra.setSubtotal(compraReqDTO.subtotal());
        compra.setDiscount(compraReqDTO.discount());
        compra.setTax(compraReqDTO.tax());
        compra.setTotal(compraReqDTO.total());
        compra.setCodStatus(compraReqDTO.codStatus());
        compra.setCreatedBy(compraReqDTO.createdBy());
        return compra;
    }

    public CompraRespDTO toRespDTO(Compra compra, List<CompraProducto> list) {
        if (compra == null) {
            return null;
        }
        return new CompraRespDTO(
                compra.getId(),
                compra.getUuid(),
                compra.getCode(),
                compra.getProveedor() != null ? ProveedorRespDTO.fromEntity(compra.getProveedor()) : null,
                compra.getAlmacen() != null ? AlmacenRespDTO.fromEntity(compra.getAlmacen()) : null,
                compra.getPurchaseDate(),
                compra.getDocumentType(),
                compra.getDocumentSeries(),
                compra.getDocumentNumber(),
                compra.getPaymentType(),
                compra.getCurrency(),
                compra.getObservation(),
                compra.getSubtotal(),
                compra.getDiscount(),
                compra.getTax(),
                compra.getTotal(),
                compra.getCodStatus(),
                compra.getCreatedBy(),
                list
        );
    }
}

package com.fgupioc.api_novasys.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ProductoReqDTO(
        String code,
        String name,
        String description,
        Long categoryId,
        Long subcategoryId,
        Long brandId,
        Long unitId,
        Long supplierId,
        String codStatus,
        BigDecimal regularPrice,
        BigDecimal wholesalePrice,
        BigDecimal salePrice,
        List<ProductoPrecioAlmacenResp> almacenes
) {
}

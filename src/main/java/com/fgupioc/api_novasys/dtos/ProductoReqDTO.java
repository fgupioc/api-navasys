package com.fgupioc.api_novasys.dtos;

public record ProductoReqDTO(
        String code,
        String name,
        String description,
        Long categoryId,
        Long subcategoryId,
        Long brandId,
        Long unitId,
        Long supplierId,
        String codStatus
) {
}

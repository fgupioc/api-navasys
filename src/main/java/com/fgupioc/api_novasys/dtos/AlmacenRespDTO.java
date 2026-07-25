package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.Almacen;

import java.util.UUID;

public record AlmacenRespDTO(
        Long id,
        UUID uuid,
        String code,
        String name,
        String description,
        String address,
        Boolean isDefault,
        String codStatus
) {
    public static AlmacenRespDTO fromEntity(Almacen warehouse) {
        if (warehouse == null) {
            return null;
        }
        return new AlmacenRespDTO(
                warehouse.getId(),
                warehouse.getUuid(),
                warehouse.getCode(),
                warehouse.getName(),
                warehouse.getDescription(),
                warehouse.getAddress(),
                warehouse.getIsDefault(),
                warehouse.getCodStatus()
        );
    }
}

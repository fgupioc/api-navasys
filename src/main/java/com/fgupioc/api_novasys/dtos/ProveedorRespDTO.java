package com.fgupioc.api_novasys.dtos;

import com.fgupioc.api_novasys.models.Proveedor;

public record ProveedorRespDTO(
        Long id,
        String ruc,
        String name,
        String contact,
        String phone,
        String codStatus
) {
    public static ProveedorRespDTO fromEntity(Proveedor proveedor) {
        return new ProveedorRespDTO(
                proveedor.getId(),
                proveedor.getRuc(),
                proveedor.getName(),
                proveedor.getContact(),
                proveedor.getPhone(),
                proveedor.getCodStatus()
        );
    }
}

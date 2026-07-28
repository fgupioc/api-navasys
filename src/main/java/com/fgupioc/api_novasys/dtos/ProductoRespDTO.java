package com.fgupioc.api_novasys.dtos;

import java.util.List;
import java.util.UUID;

public record ProductoRespDTO(
        Long id,
        UUID uuid,
        String code,
        String name,
        String description,
        CategoriaRespDTO categoria,
        CategoriaRespDTO subcategoria,
        MarcaRespDTO marca,
        UnidadRespDTO unidad,
        ProveedorRespDTO proveedor,
        List<ProductoImagenRespDTO> imagenes,
        String codStatus
) {
}

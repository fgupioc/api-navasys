package com.fgupioc.api_novasys.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 30/07/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoPrecioAlmacenResp {
    private Long warehouseId;
    private UUID warehouseUuid;
    private String warehouseName;
    private String codStatus;
    private Double stock;
    List<ProductoPrecioResp> precios = new ArrayList<>();
}

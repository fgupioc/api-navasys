package com.fgupioc.api_novasys.dtos;

import java.util.UUID;

public record ContextoResponse(
        String accessToken,
        String refreshToken,
        UUID sucursalUuid,
        String nombreSucursal
) {
}
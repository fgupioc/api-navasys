package com.fgupioc.api_novasys.dtos;

public record ClienteReqDTO(
        String documentType,
        String documentNumber,
        String firstName,
        String lastName,
        String phone,
        String codStatus
) {
}

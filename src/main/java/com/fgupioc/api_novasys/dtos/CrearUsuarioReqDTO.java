package com.fgupioc.api_novasys.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CrearUsuarioReqDTO(
        @NotBlank @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
        String document,

        @NotBlank String name,
        @NotBlank String lastname,

        @Pattern(regexp = "\\d{9}", message = "El celular debe tener 9 dígitos")
        String phone,

        @NotBlank @Size(min = 8, message = "Mínimo 8 caracteres")
        String password,

        @NotNull Long rolId,
        @NotNull List<Long> almacenIds
) {
}
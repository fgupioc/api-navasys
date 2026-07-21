package com.fgupioc.api_novasys.security.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request para refresh token")
public class RefreshTokenRequest {

    @Schema(description = "Refresh token", required = true)
    @NotBlank(message = "Refresh token es requerido")
    private String refreshToken;
}
package com.fgupioc.api_novasys.dtos;

import lombok.*;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String lastname;
    private String document;
    private String phone;
    private String codStatus;
    private String status;

    public String getStatus() {
        return "1".equalsIgnoreCase(codStatus) ? "Activo" : "Inactivo";
    }
}

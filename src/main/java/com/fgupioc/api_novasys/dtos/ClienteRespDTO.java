package com.fgupioc.api_novasys.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 23/07/2026
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteRespDTO {
    private Long id;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String phone;
    private String codStatus;

    public ClienteRespDTO(Long id, String documentType, String documentNumber, String firstName, String lastName) {
        this.id = id;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

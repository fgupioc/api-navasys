package com.fgupioc.api_novasys.models.ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaestroId implements Serializable {
    private Integer tablaId;
    private String codigo;
}
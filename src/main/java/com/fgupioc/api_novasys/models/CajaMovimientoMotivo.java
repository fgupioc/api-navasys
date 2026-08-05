package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.models.enums.TipoMovimientoCaja;
import jakarta.persistence.*;
import lombok.*;
/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 05/08/2026
 */


@Entity
@Table(
        name = "caja_movimiento_motivo",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"movement_type", "name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CajaMovimientoMotivo {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        /**
         * Tipo de movimiento al que pertenece el motivo.
         */
        @Enumerated(EnumType.STRING)
        @Column(name = "movement_type", nullable = false, length = 20)
        private TipoMovimientoCaja movementType;

        /**
         * Nombre del motivo.
         * Ejemplo:
         * - Fondo adicional
         * - Compra de útiles
         * - Pago de servicios
         */
        @Column(nullable = false, length = 100)
        private String name;

        /**
         * Descripción opcional.
         */
        @Column(length = 250)
        private String description;

        /**
         * Permite activar o desactivar el motivo.
         */
        @Builder.Default
        @Column(nullable = false)
        private Boolean active = true;

        /**
         * Orden de visualización en el combo.
         */
        @Builder.Default
        @Column(nullable = false)
        private Integer sortOrder = 1;
}

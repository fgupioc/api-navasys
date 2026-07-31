package com.fgupioc.api_novasys.models;


import com.fgupioc.api_novasys.models.enums.Alcance;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "cod_status")
    private String codStatus;

    /** Hasta dónde alcanza este rol en almacenes. No es un permiso, es un filtro de datos. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private Alcance alcance = Alcance.ALMACEN_ACTIVO;
}
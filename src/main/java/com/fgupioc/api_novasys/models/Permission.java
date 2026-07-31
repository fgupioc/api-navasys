package com.fgupioc.api_novasys.models;

import jakarta.persistence.*;
import lombok.*;
/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /** Identificador estable usado en código: "productos.crear", "ventas.anular". */
    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;
}
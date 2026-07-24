package com.fgupioc.api_novasys.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 22/07/2026
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "producto_imagenes")
@Builder
@ToString(exclude = "producto")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductoImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "es_principal")
    private Boolean esPrincipal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}
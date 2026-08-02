package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.models.enums.TipoPreciosTipo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 30/07/2026
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(
        name = "producto_precios",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_producto_precio",
                columnNames = {"producto_id", "almacen_id", "price_type"}
        )
)
@ToString(exclude = {"producto", "almacen"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProductoPrecio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false, length = 30)
    private TipoPreciosTipo priceType;

    @Column(name = "sale_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal salePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** NULL = precio general (aplica a todos los almacenes) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id")
    private Almacen almacen;

    @Column(name = "minimum_quantity")
    private Integer minimumQuantity;
    private String codStatus;
}
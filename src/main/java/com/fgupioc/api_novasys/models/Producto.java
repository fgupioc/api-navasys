package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
@Builder
@Table(name = "productos")
@SQLDelete(sql = "UPDATE productos SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@ToString(exclude = {"categoria", "subcategoria", "marca", "unidad", "proveedor", "imagenes"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Producto extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    private String barcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Categoria subcategoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unidad unidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = true)
    private Proveedor proveedor;

    @Column(name = "cod_status")
    private String codStatus;

    @Column(name = "last_cost")
    private BigDecimal lastCost;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "minimum_sale_price")
    private BigDecimal minimumSalePrice; // opcional

    @Column(name = "wholesale_price")
    private BigDecimal wholesalePrice;   // opcional

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductoImagen> imagenes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
        this.codStatus = CONST.STATUS_ACTIVE;
    }
}
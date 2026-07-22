package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;


/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 21/07/2026
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "categorias")
@SQLDelete(sql = "UPDATE categorias SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Categoria extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String code;
    @Column(nullable = false, length = 100)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Categoria parent;
    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Categoria> children = new ArrayList<>();
    @Column(nullable = false, name = "cod_status")
    private String codStatus;

    @PrePersist
    public void prePersist() {
        codStatus = CONST.STATUS_ACTIVE;
    }

}

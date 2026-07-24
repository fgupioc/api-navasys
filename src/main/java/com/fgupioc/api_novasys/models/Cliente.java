package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.helpers.CONST;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 23/07/2026
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "clientes")
@SQLDelete(sql = "UPDATE clientes SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Cliente extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String phone;
    private String codStatus;

    @PrePersist
    public void prePersist() {
        this.codStatus = CONST.STATUS_ACTIVE;
    }
}

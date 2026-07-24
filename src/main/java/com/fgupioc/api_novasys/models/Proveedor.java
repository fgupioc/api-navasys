package com.fgupioc.api_novasys.models;

import com.fgupioc.api_novasys.dtos.ProveedorReqDTO;
import com.fgupioc.api_novasys.dtos.ProveedorRespDTO;
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
 * @since 22/07/2026
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "proveedores")
@SQLDelete(sql = "UPDATE proveedores SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Proveedor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ruc;
    private String name;
    private String contact;
    private String phone;
    @Column(name = "cod_status")
    private String codStatus;

    public static ProveedorRespDTO toRespDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }
        return new ProveedorRespDTO(
                proveedor.getId(),
                proveedor.getRuc(),
                proveedor.getName(),
                proveedor.getContact(),
                proveedor.getPhone(),
                proveedor.getCodStatus()
        );
    }

    @PrePersist
    public void prePersist() {
        codStatus = CONST.STATUS_ACTIVE;
    }

    public static Proveedor fromDto(ProveedorReqDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setRuc(dto.ruc());
        proveedor.setName(dto.name());
        proveedor.setContact(dto.contact());
        proveedor.setPhone(dto.phone());
        proveedor.setCodStatus(dto.codStatus());
        return proveedor;
    }

    public void updateFromDto(ProveedorReqDTO dto) {
        this.setRuc(dto.ruc());
        this.setName(dto.name());
        this.setContact(dto.contact());
        this.setPhone(dto.phone());
        this.setCodStatus(dto.codStatus());
    }
}

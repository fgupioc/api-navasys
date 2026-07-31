package com.fgupioc.api_novasys.models.ids;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserPermissionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "permission_id")
    private Long permissionId;
}

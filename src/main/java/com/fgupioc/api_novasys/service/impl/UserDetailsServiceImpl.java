package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.CrearUsuarioReqDTO;
import com.fgupioc.api_novasys.helpers.BusinessException;
import com.fgupioc.api_novasys.helpers.CONST;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.enums.Alcance;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.security.config.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UsuarioAlmacenRepository usuarioAlmacenRepository;
    private final AlmacenRepository almacenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TiendaRepository tiendaRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String document) {
        User u = userRepository.findByDocumentWithRol(document)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + document));

        return new UserPrincipal(
                u.getId(),
                u.getTienda() != null ? u.getTienda().getId() : null,
                u.getDocument(),
                u.getPassword(),
                u.getRol().getName(),
                u.getRol().getAlcance(),
                permissionRepository.resolverPorUsuario(u.getId(), u.getRol().getId()),
                CONST.STATUS_ACTIVE.equals(u.getCodStatus())
        );
    }


    @Transactional
    @PreAuthorize("hasAuthority('usuarios.gestionar')")
    public void crear(CrearUsuarioReqDTO req) {

        if (userRepository.existsByDocument(req.document())) {
            throw new BusinessException("Ya existe un usuario con el DNI " + req.document());
        }

        Role role = roleRepository.findById(req.rolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        UserPrincipal actor = SecurityUtil.principal();

        User user = User.builder()
                .document(req.document())
                .name(req.name())
                .lastname(req.lastname())
                .phone(req.phone())
                .password(passwordEncoder.encode(req.password()))
                .rol(role)
                .tienda(tiendaRepository.getReferenceById(actor.branchId()))
                .build();

        userRepository.save(user);
        asignarAlmacenes(user, req.almacenIds(), role);

    }

    private void asignarAlmacenes(User user, List<Long> almacenIds, Role role) {
        if (role.getAlcance() == Alcance.TODOS) {
            return;
        }
        if (almacenIds == null || almacenIds.isEmpty()) {
            throw new BusinessException(
                    "Un usuario con rol " + role.getName() + " necesita al menos un almacén asignado");
        }

        List<Almacen> almacenes = almacenRepository.findAllById(almacenIds);
        if (almacenes.size() != almacenIds.size()) {
            throw new BusinessException("Uno o más almacenes no existen");
        }

        for (int i = 0; i < almacenes.size(); i++) {
            usuarioAlmacenRepository.save(UsuarioAlmacen.builder()
                    .user(user)
                    .almacen(almacenes.get(i))
                    .isDefault(i == 0)
                    .build());
        }
    }
}
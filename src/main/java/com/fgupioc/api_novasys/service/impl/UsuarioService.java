package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.CajaRespDTO;
import com.fgupioc.api_novasys.dtos.CajaSesionRespDTO;
import com.fgupioc.api_novasys.dtos.CrearUsuarioReqDTO;
import com.fgupioc.api_novasys.dtos.UserDto;
import com.fgupioc.api_novasys.helpers.BusinessException;
import com.fgupioc.api_novasys.helpers.CONST;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.enums.Alcance;
import com.fgupioc.api_novasys.models.interfaces.IUsuarioAlmacen;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.security.config.SecurityUtil;
import com.fgupioc.api_novasys.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
public class UsuarioService implements UserDetailsService, IUsuarioService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UsuarioAlmacenRepository usuarioAlmacenRepository;
    private final AlmacenRepository almacenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TiendaRepository tiendaRepository;
    private final PermissionRepository permissionRepository;
    private final CajaSesionRepository cajaSesionRepository;

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

    @Transactional(readOnly = true)
    @Override
    public List<IUsuarioAlmacen> almacenes(Authentication authentication) throws RuntimeException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (principal == null) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }
        User user = userRepository.findById(principal.id())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return usuarioAlmacenRepository.findByUserId(user.getId());
    }

    @Override
    public IUsuarioAlmacen miAlmacen(Authentication authentication, Long almacenId) throws RuntimeException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (principal == null) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }
        User user = userRepository.findById(principal.id())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return usuarioAlmacenRepository.findByUserIdAndAlmacenId(user.getId(), almacenId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Override
    public UserDto findByDocument(String document) throws RuntimeException {
        User user = userRepository.findByDocument(document)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .document(user.getDocument())
                .phone(user.getPhone())
                .codStatus(user.getCodStatus())
                .status(user.getStatus())
                .role(user.getRol().getName())
                .build();
    }

    @Override
    public CajaRespDTO miCajaActiva(Authentication authentication) throws RuntimeException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (principal == null) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }
        User user = userRepository.findById(principal.id())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return cajaSesionRepository.findByCajaOpenByUser(user.getId())
                .map(cajaSesion -> CajaRespDTO.builder()
                        .uuid(cajaSesion.getCaja().getUuid())
                        .code(cajaSesion.getCaja().getCode())
                        .name(cajaSesion.getCaja().getName())
                        .description(cajaSesion.getCaja().getDescription())
                        .status(cajaSesion.getStatus())
                        .cajaSesion(CajaSesionRespDTO.builder()
                                .cajaSesionId(cajaSesion.getId())
                                .openedAt(cajaSesion.getOpenedAt())
                                .closedAt(cajaSesion.getClosedAt())
                                .openingAmount(cajaSesion.getOpeningAmount())
                                .closingAmount(cajaSesion.getClosingAmount())
                                .status(cajaSesion.getStatus())
                                .notes(cajaSesion.getNotes())
                                .build()
                        ).build())
                .orElseThrow(() -> new ResourceNotFoundException("No hay caja activa para el usuario"));
    }
}
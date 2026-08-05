package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.*;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.enums.EstadoSesionCaja;
import com.fgupioc.api_novasys.models.enums.TipoMovimientoCaja;
import com.fgupioc.api_novasys.models.interfaces.IVentaTotales;
import com.fgupioc.api_novasys.models.interfaces.IVentasDiarias;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.service.ICajaService;
import com.fgupioc.api_novasys.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 05/08/2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CajaService implements ICajaService {
    private final CajaRepository cajaRepository;
    private final UserRepository userRepository;
    private final AlmacenRepository almacenRepository;
    private final CajaSesionRepository cajaSesionRepository;
    private final VentaRepository ventaRepository;
    private final IUsuarioService usuarioService;
    private final CajaMovimientoRepository cajaMovimientoRepository;
    private final CajaMovimientoMotivoRepository cajaMovimientoMotivoRepository;

    @Override
    public List<CajaRespDTO> listarCajasPorAlmacen(Authentication authentication) throws RuntimeException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (principal == null) {
            throw new ResourceNotFoundException("Usuario no autenticado");
        }
        Almacen almacen = almacenRepository.findById(principal.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado: " + principal.branchId()));
        return cajaRepository.listaCajasPorAlmacen(almacen)
                .stream()
                .map(caja -> new CajaRespDTO(caja.getUuid(), caja.getCode(), caja.getName(), caja.getDescription(), caja.getStatus()))
                .toList();
    }

    @Override
    public void abrirCaja(CajaSesionReqDTO cajaSesion, Authentication authentication) throws RuntimeException {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (principal == null) {
                throw new ResourceNotFoundException("Usuario no autenticado");
            }
            User user = userRepository.findById(principal.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + principal.id()));
            Caja caja = cajaRepository.findByUuid((cajaSesion.getCajaUuid()))
                    .orElseThrow(() -> new ResourceNotFoundException("Caja no encontrada: " + cajaSesion.getCajaUuid()));
            if (caja.getStatus().equals(EstadoSesionCaja.OPEN)) {
                throw new RuntimeException("La caja ya está abierta. No se puede iniciar una nueva sesión de caja.");
            }

            caja.setStatus(EstadoSesionCaja.OPEN);
            CajaSesion cajaSesionEntity = new CajaSesion();
            cajaSesionEntity.setUser(user);
            cajaSesionEntity.setCaja(caja);
            cajaSesionEntity.setOpenedAt(LocalDateTime.now());
            cajaSesionEntity.setOpeningAmount(cajaSesion.getOpeningAmount());
            cajaSesionEntity.setClosingAmount(BigDecimal.ZERO);
            cajaSesionEntity.setStatus(EstadoSesionCaja.OPEN);
            cajaSesionEntity.setNotes("");
            cajaSesionEntity.setSalesCount(0);
            cajaSesionEntity.setCollectionsAmount(BigDecimal.ZERO);
            cajaSesionEntity.setMovementsCount(0);
            cajaSesionEntity.setPhysicalCashAmount(BigDecimal.ZERO);
            cajaSesionEntity.setExecutiveCashAmount(BigDecimal.ZERO);
            cajaSesionEntity.setDigitalWalletAmount(BigDecimal.ZERO);
            cajaSesionEntity.setTotalPaymentsAmount(BigDecimal.ZERO);
            cajaSesionEntity.setCashInAmount(BigDecimal.ZERO);
            cajaSesionEntity.setCashOutAmount(BigDecimal.ZERO);
            cajaSesionEntity.setCashSalesAmount(BigDecimal.ZERO);
            cajaSesionEntity.setExpectedCashAmount(BigDecimal.ZERO);
            cajaSesionEntity.setCountedCashAmount(BigDecimal.ZERO);
            cajaSesionEntity.setCashDifferenceAmount(BigDecimal.ZERO);

            cajaSesionRepository.save(cajaSesionEntity);
        } catch (RuntimeException e) {
            log.error("Error al abrir la caja: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void registrarCierreCaja(CajaSesionReqDTO cajaSesion, Authentication authentication) throws RuntimeException {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (principal == null) {
                throw new ResourceNotFoundException("Usuario no autenticado");
            }
            User user = userRepository.findById(principal.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + principal.id()));
            Caja caja = cajaRepository.findByUuid((cajaSesion.getCajaUuid()))
                    .orElseThrow(() -> new ResourceNotFoundException("Caja no encontrada: " + cajaSesion.getCajaUuid()));
            if (caja.getStatus().equals(EstadoSesionCaja.CLOSED)) {
                throw new RuntimeException("La caja ya está cerrada. No se puede cerrar una sesión de caja que ya está cerrada.");
            }

            CajaSesion cajaSesionEntity = cajaSesionRepository.findById(cajaSesion.getCajaSesionId())
                    .orElseThrow(() -> new RuntimeException("No hay una sesión de caja abierta para esta caja."));

            caja.setStatus(EstadoSesionCaja.CLOSED);
            cajaSesionEntity.setClosedAt(LocalDateTime.now());
            cajaSesionEntity.setClosingAmount(cajaSesion.getClosingAmount());
            cajaSesionEntity.setStatus(EstadoSesionCaja.CLOSED);
            cajaSesionEntity.setNotes(cajaSesion.getNotes());

            cajaSesionEntity.setOpeningAmount(cajaSesion.getOpeningAmount());
            cajaSesionEntity.setSalesCount(cajaSesion.getSalesCount());
            cajaSesionEntity.setCollectionsAmount(cajaSesion.getCollectionsAmount());
            cajaSesionEntity.setMovementsCount(cajaSesion.getMovementsCount());
            cajaSesionEntity.setPhysicalCashAmount(cajaSesion.getPhysicalCashAmount());
            cajaSesionEntity.setExecutiveCashAmount(cajaSesion.getExecutiveCashAmount());
            cajaSesionEntity.setDigitalWalletAmount(cajaSesion.getDigitalWalletAmount());
            cajaSesionEntity.setTotalPaymentsAmount(cajaSesion.getTotalPaymentsAmount());
            cajaSesionEntity.setCashInAmount(cajaSesion.getCashInAmount());
            cajaSesionEntity.setCashOutAmount(cajaSesion.getCashOutAmount());
            cajaSesionEntity.setCashSalesAmount(cajaSesion.getCashSalesAmount());
            cajaSesionEntity.setExpectedCashAmount(cajaSesion.getExpectedCashAmount());
            cajaSesionEntity.setCountedCashAmount(cajaSesion.getCountedCashAmount());
            cajaSesionEntity.setCashDifferenceAmount(cajaSesion.getCashDifferenceAmount());

            cajaSesionRepository.save(cajaSesionEntity);
        } catch (RuntimeException e) {
            log.error("Error al registrar el cierre de la caja: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public CajaSesionRespDTO obtenerInfoParaCierreCaja(Authentication authentication) throws RuntimeException {
        try {
            CajaRespDTO miCaja = usuarioService.miCajaActiva(authentication);
            Caja caja = cajaRepository.findByUuid(miCaja.getUuid())
                    .orElseThrow(() -> new ResourceNotFoundException("Caja no encontrada: " + miCaja.getUuid()));

            CajaSesion cajaSesionDB = cajaSesionRepository.findById(miCaja.getCajaSesion().getCajaSesionId()).orElseThrow(() -> new ResourceNotFoundException("No hay una sesión de caja abierta para esta caja."));

            List<IVentasDiarias> ventas = ventaRepository.findVentasDiarias(miCaja.getCajaSesion().getCajaSesionId());

            IVentaTotales totales = ventaRepository.obtenerMontosTotales(miCaja.getCajaSesion().getCajaSesionId());
            List<CajaMovimiento> movimientos = cajaMovimientoRepository.findByCajaSesionId(miCaja.getCajaSesion().getCajaSesionId());
            BigDecimal ingresos = movimientos.stream()
                    .filter(m -> m.getType().equals(TipoMovimientoCaja.INCOME))
                    .map(CajaMovimiento::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal gastos = movimientos.stream()
                    .filter(m -> m.getType().equals(TipoMovimientoCaja.EXPENSE))
                    .map(CajaMovimiento::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalMedios = totales.getTotalEfectivo().add(totales.getTotalYape());
            BigDecimal efectivoEsperado = cajaSesionDB.getOpeningAmount().add(totales.getTotalEfectivo()).add(ingresos).subtract(gastos);

            CajaSesionRespDTO cajaSesion = new CajaSesionRespDTO();
            cajaSesion.setSalesCount(ventas.size()); // Ventas
            cajaSesion.setCollectionsAmount(cajaSesionDB.getCollectionsAmount()); // Cobros
            cajaSesion.setMovementsCount(cajaSesionDB.getMovementsCount()); // Movimientos
            cajaSesion.setPhysicalCashAmount(cajaSesionDB.getPhysicalCashAmount()); // Efectivo en caja física
            cajaSesion.setExecutiveCashAmount(totales.getTotalEfectivo()); // Efectivo
            cajaSesion.setDigitalWalletAmount(totales.getTotalYape()); // Yape/Plin
            cajaSesion.setTotalPaymentsAmount(totales.getTotalGeneral()); // Total pagos
            cajaSesion.setOpeningAmount(cajaSesionDB.getOpeningAmount()); // Fondo inicial
            cajaSesion.setCashInAmount(ingresos); // Ingresos
            cajaSesion.setCashOutAmount(gastos); // Egresos
            cajaSesion.setCashSalesAmount(totales.getTotalEfectivo()); // Ventas en efectivo
            cajaSesion.setExpectedCashAmount(efectivoEsperado); // Efectivo esperado
            cajaSesion.setCountedCashAmount(cajaSesionDB.getCountedCashAmount()); // Efectivo contado
            cajaSesion.setCashDifferenceAmount(cajaSesionDB.getCashDifferenceAmount()); // Diferencia
            cajaSesion.setCajaSesionId(miCaja.getCajaSesion().getCajaSesionId());
            cajaSesion.setCajaUuid(caja.getUuid());
            cajaSesion.setClosingAmount(cajaSesionDB.getClosingAmount()); //
            cajaSesion.setNotes(cajaSesionDB.getNotes());

            return cajaSesion;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CajaMovimientoRespDTO> listarMovimientosCaja(Authentication authentication) throws RuntimeException {
        CajaRespDTO caja = usuarioService.miCajaActiva(authentication);
        return cajaMovimientoRepository.findByCajaSesionId(caja.getCajaSesion().getCajaSesionId())
                .stream()
                .map(m -> new CajaMovimientoRespDTO(
                        m.getId(),
                        m.getType(),
                        m.getReason().getName(),
                        m.getDescription(),
                        m.getAmount(),
                        m.getUser().getName(),
                        m.getMovementDate()
                ))
                .toList();
    }

    @Override
    public void registrarCajaMovimiento(CajaMovimientoReqDTO cajaMovimientoReqDTO, Authentication authentication) throws RuntimeException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (principal == null) {
            throw new ResourceNotFoundException("Usuario no autenticado");
        }

        CajaRespDTO caja = usuarioService.miCajaActiva(authentication);
        CajaSesion cajaSesion = cajaSesionRepository.findById(caja.getCajaSesion().getCajaSesionId()).orElseThrow(() -> new ResourceNotFoundException("CajaSesion not found"));
        User user = userRepository.findById(principal.id()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CajaMovimientoMotivo cajaMovimientoMotivo = cajaMovimientoMotivoRepository.findById(cajaMovimientoReqDTO.getReasonId())
                .orElseThrow(() -> new ResourceNotFoundException("CajaMovimientoMotivo not found"));

        CajaMovimiento cajaMovimiento = new CajaMovimiento();
        cajaMovimiento.setCajaSesion(cajaSesion);
        cajaMovimiento.setUser(user);
        cajaMovimiento.setType(cajaMovimientoReqDTO.getType());
        cajaMovimiento.setReason(cajaMovimientoMotivo);
        cajaMovimiento.setDescription(cajaMovimientoReqDTO.getDescription());
        cajaMovimiento.setAmount(cajaMovimientoReqDTO.getAmount());
        cajaMovimiento.setMovementDate(LocalDateTime.now());

        cajaMovimientoRepository.save(cajaMovimiento);
    }

    @Override
    public void eliminarCajaMovimiento(Long cajaMovimientoId, Authentication authentication) throws RuntimeException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (principal == null) {
            throw new ResourceNotFoundException("Usuario no autenticado");
        }

        CajaMovimiento cajaMovimiento = cajaMovimientoRepository.findById(cajaMovimientoId)
                .orElseThrow(() -> new ResourceNotFoundException("CajaMovimiento not found"));

        cajaMovimientoRepository.delete(cajaMovimiento);
    }

    @Override
    public List<CajaMovimientoMotivoRespDTO> listarMotivosMovimientoCaja() throws RuntimeException {
        return cajaMovimientoMotivoRepository.findAllByActiveTrueOrderByMovementTypeNameAsc()
                .stream()
                .map(motivo -> new CajaMovimientoMotivoRespDTO(
                        motivo.getId(),
                        motivo.getMovementType(),
                        motivo.getName(),
                        motivo.getDescription(),
                        motivo.getActive(),
                        motivo.getSortOrder()
                ))
                .toList();
    }
}

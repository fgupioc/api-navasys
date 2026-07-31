package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.AlmacenRespDTO;
import com.fgupioc.api_novasys.dtos.MaestroRespDTO;
import com.fgupioc.api_novasys.models.Maestro;
import com.fgupioc.api_novasys.models.Tienda;
import com.fgupioc.api_novasys.repository.AlmacenRepository;
import com.fgupioc.api_novasys.repository.MaestroRepository;
import com.fgupioc.api_novasys.repository.TiendaRepository;
import com.fgupioc.api_novasys.service.IMaestroService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fgupioc.api_novasys.models.Maestro.*;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@Service
@RequiredArgsConstructor
public class MaestroService implements IMaestroService {
    private final ModelMapper modelMapper;
    private final MaestroRepository maestroRepository;
    private final TiendaRepository tiendaRepository;
    private final AlmacenRepository almacenRepository;

    @Override
    public List<MaestroRespDTO> listarEstados() throws RuntimeException {
        maestroRepository.findByIdTablaId(ESTADO);
        return maestroRepository.findByIdTablaId(ESTADO)
                .stream()
                .filter(maestro -> !maestro.getId().getCodigo().equals("-"))
                .map(Maestro::toRespDTO)
                .toList();
    }

    @Override
    public List<MaestroRespDTO> listarTipoUnidades() throws RuntimeException {
        return maestroRepository.findByIdTablaId(TIPO_UNIDAD)
                .stream()
                .filter(maestro -> !maestro.getId().getCodigo().equals("-"))
                .map(Maestro::toRespDTO)
                .toList();
    }

    @Override
    public List<MaestroRespDTO> listarEstadosDeCompra() throws RuntimeException {
        return maestroRepository.findByIdTablaId(ESTADO_DE_COMPRA)
                .stream()
                .filter(maestro -> !maestro.getId().getCodigo().equals("-"))
                .map(Maestro::toRespDTO)
                .toList();
    }

    @Override
    public List<MaestroRespDTO> listarEstadosDeVenta() throws RuntimeException {
        return maestroRepository.findByIdTablaId(ESTADO_DE_VENTA)
                .stream()
                .filter(maestro -> !maestro.getId().getCodigo().equals("-"))
                .map(Maestro::toRespDTO)
                .toList();
    }

    @Override
    public List<AlmacenRespDTO> listarAlmacenes(long tiendaId) throws RuntimeException {
        Tienda tienda = tiendaRepository.findById(tiendaId).orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        return almacenRepository.findAllByTienda(tienda).stream().map(AlmacenRespDTO::fromEntity).toList();
    }
}

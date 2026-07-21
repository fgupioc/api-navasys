package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.MaestroRespDTO;
import com.fgupioc.api_novasys.models.Maestro;
import com.fgupioc.api_novasys.repository.MaestroRepository;
import com.fgupioc.api_novasys.service.IMaestroService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fgupioc.api_novasys.models.Maestro.ESTADO;
import static com.fgupioc.api_novasys.models.Maestro.TIPO_UNIDAD;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@Service
@RequiredArgsConstructor
public class MaestroService implements IMaestroService {
    private final MaestroRepository maestroRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<MaestroRespDTO> listarEstados() throws RuntimeException {
        maestroRepository.findByIdTablaId(ESTADO);
        return maestroRepository.findByIdTablaId(ESTADO)
                .stream()
                .map(Maestro::toRespDTO)
                .toList();
    }

    @Override
    public List<MaestroRespDTO> listarTipoUnidades() throws RuntimeException {
        return maestroRepository.findByIdTablaId(TIPO_UNIDAD)
                .stream()
                .map(Maestro::toRespDTO)
                .toList();
    }
}

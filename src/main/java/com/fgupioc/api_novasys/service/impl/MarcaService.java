package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.MarcaReqDTO;
import com.fgupioc.api_novasys.dtos.MarcaRespDTO;
import com.fgupioc.api_novasys.helpers.DuplicateResourceException;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.Marca;
import com.fgupioc.api_novasys.repository.MarcaRepository;
import com.fgupioc.api_novasys.service.IMarcaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@Service
@RequiredArgsConstructor
public class MarcaService implements IMarcaService {
    private final MarcaRepository unidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MarcaRespDTO> findAll() {
        return unidadRepository.findAll()
                .stream()
                .map(Marca::toRespDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaRespDTO findById(Long id) {
        Marca unidad = findEntityById(id);
        return Marca.toRespDTO(unidad);
    }

    @Override
    @Transactional
    public MarcaRespDTO create(MarcaReqDTO dto) {
        if (unidadRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Ya existe una unidad con el código: " + dto.getCode());
        }

        Marca unidad = new Marca();
        unidad.setCode(dto.getCode());
        unidad.setName(dto.getName());

        Marca saved = unidadRepository.save(unidad);
        return Marca.toRespDTO(saved);
    }

    @Override
    @Transactional
    public MarcaRespDTO update(Long id, MarcaReqDTO dto) {
        Marca unidad = findEntityById(id);

        if (!unidad.getCode().equals(dto.getCode()) && unidadRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Ya existe una unidad con el código: " + dto.getCode());
        }

        unidad.setCode(dto.getCode());
        unidad.setName(dto.getName());
        unidad.setCodStatus(dto.getCodStatus());

        Marca updated = unidadRepository.save(unidad);
        return Marca.toRespDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Marca unidad = findEntityById(id);
        unidadRepository.delete(unidad);
    }

    private Marca findEntityById(Long id) {
        return unidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada con id: " + id));
    }
}

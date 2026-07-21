package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.UnidadReqDTO;
import com.fgupioc.api_novasys.dtos.UnidadRespDTO;
import com.fgupioc.api_novasys.helpers.DuplicateResourceException;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.Unidad;
import com.fgupioc.api_novasys.repository.UnidadRepository;
import com.fgupioc.api_novasys.service.IUnidadService;
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
public class UnidadService implements IUnidadService {
    private final UnidadRepository unidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UnidadRespDTO> findAll() {
        return unidadRepository.findAll()
                .stream()
                .map(Unidad::toRespDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadRespDTO findById(Long id) {
        Unidad unidad = findEntityById(id);
        return Unidad.toRespDTO(unidad);
    }

    @Override
    @Transactional
    public UnidadRespDTO create(UnidadReqDTO dto) {
        if (unidadRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Ya existe una unidad con el código: " + dto.getCode());
        }

        Unidad unidad = new Unidad();
        unidad.setCode(dto.getCode());
        unidad.setName(dto.getName());
        unidad.setCodType(dto.getCodType());

        Unidad saved = unidadRepository.save(unidad);
        return Unidad.toRespDTO(saved);
    }

    @Override
    @Transactional
    public UnidadRespDTO update(Long id, UnidadReqDTO dto) {
        Unidad unidad = findEntityById(id);

        if (!unidad.getCode().equals(dto.getCode()) && unidadRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("Ya existe una unidad con el código: " + dto.getCode());
        }

        unidad.setCode(dto.getCode());
        unidad.setName(dto.getName());
        unidad.setCodType(dto.getCodType());
        unidad.setCodStatus(dto.getCodStatus());

        Unidad updated = unidadRepository.save(unidad);
        return Unidad.toRespDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Unidad unidad = findEntityById(id);
        unidadRepository.delete(unidad);
    }

    private Unidad findEntityById(Long id) {
        return unidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada con id: " + id));
    }
}

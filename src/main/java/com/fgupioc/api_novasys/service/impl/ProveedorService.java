package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.ProveedorReqDTO;
import com.fgupioc.api_novasys.dtos.ProveedorRespDTO;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.Proveedor;
import com.fgupioc.api_novasys.repository.ProveedorRepository;
import com.fgupioc.api_novasys.service.IProveedorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 22/07/2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProveedorService implements IProveedorService {
    private final ProveedorRepository proveedorRepository;

    @Override
    public List<ProveedorRespDTO> findAll() throws RuntimeException {
        return proveedorRepository.findAllByOrderByIdDesc().stream().map(ProveedorRespDTO::fromEntity).toList();
    }

    @Override
    public ProveedorRespDTO findById(Long id) throws RuntimeException {
        return proveedorRepository.findById(id).map(ProveedorRespDTO::fromEntity).orElseThrow(() -> new ResourceNotFoundException("Proveedor not found with id: " + id));
    }

    @Override
    public ProveedorRespDTO create(ProveedorReqDTO dto) throws RuntimeException {
        proveedorRepository.findByRuc(dto.ruc()).ifPresent(proveedor -> {
            throw new RuntimeException("Proveedor already exists with RUC: " + dto.ruc());
        });
        Proveedor proveedor = Proveedor.fromDto(dto);
        proveedorRepository.save(proveedor);
        return ProveedorRespDTO.fromEntity(proveedor);
    }

    @Override
    public ProveedorRespDTO update(Long id, ProveedorReqDTO dto) throws RuntimeException {
        proveedorRepository.findByRucDistinct(dto.ruc(), id).ifPresent(proveedor -> {
            throw new RuntimeException("Proveedor already exists with RUC: " + dto.ruc());
        });
        Proveedor proveedor = proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor not found with id: " + id));
        proveedor.updateFromDto(dto);
        proveedorRepository.save(proveedor);
        return ProveedorRespDTO.fromEntity(proveedor);
    }

    @Override
    public void delete(Long id) throws RuntimeException {
        Proveedor proveedor = proveedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proveedor not found with id: " + id));
        proveedorRepository.delete(proveedor);
    }
}

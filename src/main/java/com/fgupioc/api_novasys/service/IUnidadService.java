package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.UnidadReqDTO;
import com.fgupioc.api_novasys.dtos.UnidadRespDTO;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */
public interface IUnidadService {
    List<UnidadRespDTO> findAll();

    UnidadRespDTO findById(Long id);

    UnidadRespDTO create(UnidadReqDTO dto);

    UnidadRespDTO update(Long id, UnidadReqDTO dto);

    void delete(Long id);
}

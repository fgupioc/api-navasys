package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.MarcaReqDTO;
import com.fgupioc.api_novasys.dtos.MarcaRespDTO;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */
public interface IMarcaService {
    List<MarcaRespDTO> findAll();

    MarcaRespDTO findById(Long id);

    MarcaRespDTO create(MarcaReqDTO dto);

    MarcaRespDTO update(Long id, MarcaReqDTO dto);

    void delete(Long id);
}

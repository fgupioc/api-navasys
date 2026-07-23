package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.ProveedorReqDTO;
import com.fgupioc.api_novasys.dtos.ProveedorRespDTO;

import java.util.List;

public interface IProveedorService {
    List<ProveedorRespDTO> findAll();

    ProveedorRespDTO findById(Long id);

    ProveedorRespDTO create(ProveedorReqDTO dto);

    ProveedorRespDTO update(Long id, ProveedorReqDTO dto);

    void delete(Long id);
}

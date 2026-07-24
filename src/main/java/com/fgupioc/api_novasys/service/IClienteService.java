package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.ClienteReqDTO;
import com.fgupioc.api_novasys.dtos.ClienteRespDTO;

import java.util.List;

public interface IClienteService {
    List<ClienteRespDTO> findAll() throws RuntimeException;

    ClienteRespDTO findById(Long id) throws RuntimeException;

    ClienteRespDTO create(ClienteReqDTO dto) throws RuntimeException;

    ClienteRespDTO update(Long id, ClienteReqDTO dto) throws RuntimeException;

    void delete(Long id) throws RuntimeException;
}

package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.MaestroRespDTO;

import java.util.List;

public interface IMaestroService {
    List<MaestroRespDTO> listarTipoUnidades() throws RuntimeException;

    List<MaestroRespDTO> listarEstados() throws RuntimeException;
}

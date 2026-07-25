package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.CompraReqDTO;
import com.fgupioc.api_novasys.dtos.CompraRespDTO;
import com.fgupioc.api_novasys.models.interfaces.CompraListado;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ICompraService {
    CompraRespDTO registrarCompra(CompraReqDTO compraReqDTO) throws RuntimeException;

    List<CompraListado> listarCompras() throws RuntimeException;

    List<CompraListado> filtrarCompras(LocalDate desde, LocalDate hasta, Long supplierId, String codStatus) throws RuntimeException;

    CompraRespDTO buscarCompra(UUID uuid) throws RuntimeException;

    CompraRespDTO actualizarCompra(UUID uuid, CompraReqDTO compraReqDTO) throws RuntimeException;
}

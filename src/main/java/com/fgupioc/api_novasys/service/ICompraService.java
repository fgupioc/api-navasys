package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.CompraReqDTO;
import com.fgupioc.api_novasys.dtos.CompraRespDTO;

public interface ICompraService {
    CompraRespDTO registrarCompra(CompraReqDTO compraRespDTO) throws RuntimeException;
}

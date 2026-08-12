package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.TransferenciaReqDTO;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface ITransferenciaService {
    UUID registrarTransferencia(TransferenciaReqDTO transferenciaReqDTO, Authentication authentication) throws RuntimeException;

    UUID actualizarTransferencia(UUID uuid, TransferenciaReqDTO transferenciaReqDTO, Authentication authentication) throws RuntimeException;
}

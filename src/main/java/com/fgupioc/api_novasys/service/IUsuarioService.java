package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.UserDto;
import com.fgupioc.api_novasys.models.interfaces.IUsuarioAlmacen;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */
public interface IUsuarioService {
    List<IUsuarioAlmacen> almacenes(Authentication authentication) throws RuntimeException;

    IUsuarioAlmacen miAlmacen(Authentication authentication, Long almacenId) throws RuntimeException;

    UserDto findByDocument(String document) throws RuntimeException;
}

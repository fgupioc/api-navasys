package com.fgupioc.api_novasys.dtos;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 21/07/2026
 */
public record CategoriaRespDTO(
        Long id,
        String code,
        String name,
        String codStatus,
        CategoriaParentRespDTO parent
) {
    public CategoriaRespDTO(Long id, String code, String name) {
        this(id, code, name, "", null);
    }
}
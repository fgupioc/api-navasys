package com.fgupioc.api_novasys.helpers;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 01/08/2026
 */
public class SucursalNoAsignadaException extends RuntimeException {
    public SucursalNoAsignadaException(Long sucursalId) {
        super("No tienes acceso a la sucursal " + sucursalId);
    }
}
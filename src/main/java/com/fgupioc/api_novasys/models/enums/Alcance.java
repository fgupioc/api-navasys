package com.fgupioc.api_novasys.models.enums;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 31/07/2026
 */
public enum Alcance {

    /** Solo el almacén de la caja que tiene abierta ahora. Vendedor, cajero. */
    ALMACEN_ACTIVO,

    /** Todos los almacenes que tiene asignados en usuario_almacen. Jefe de sucursal. */
    ALMACENES_ASIGNADOS,

    /** Todos los almacenes de su tienda. Administrador, contador. */
    TODOS
}

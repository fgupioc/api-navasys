package com.fgupioc.api_novasys.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 25/07/2026
 */
@Getter
@Setter
@Builder
public class OrdenCompraPdfDTO {
    // Empresa (esto puede venir de application.yml o de una tabla "empresa")
    private String empresaNombre;
    private String empresaDireccion;
    private String empresaRuc;
    private String empresaTelefono;

    // Cabecera orden
    private String numeroOrden;      // 001-0001234
    private LocalDate fecha;

    // Proveedor
    private String proveedorNombre;
    private String proveedorRuc;
    private String proveedorDireccion;
    private String proveedorTelefono;

    // Estado / datos compra
    private String codEstado;           // Registrada
    private String estado;           // Registrada
    private LocalDate fechaCompra;
    private String documentoRef;     // Cotización N° 00045
    private String almacen;

    private List<ItemDetalleDTO> items;
    private String observaciones;

    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;

    private String elaboradoPor;
    private String elaboradoRol;
    private String aprobadoPor;
    private String aprobadoRol;

    private String qrBase64; // imagen QR en base64, sin el prefijo data:image
}
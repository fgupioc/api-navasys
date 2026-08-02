package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.ProductoReqDTO;
import com.fgupioc.api_novasys.dtos.ProductoRespDTO;
import com.fgupioc.api_novasys.models.Producto;
import com.fgupioc.api_novasys.models.interfaces.IProductoAlmacenList;
import com.fgupioc.api_novasys.models.interfaces.ProductoAutocompledado;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 22/07/2026
 */
public interface IProductoService {

    ProductoRespDTO buscarPorUuid(UUID uuid) throws RuntimeException;

    Producto registrar(ProductoReqDTO request, List<MultipartFile> imagenes) throws RuntimeException;

    Producto actualizar(UUID uuid, ProductoReqDTO request, List<MultipartFile> imagenes) throws RuntimeException;

    List<ProductoRespDTO> listarProductos() throws RuntimeException;

    void eliminar(UUID uuid) throws RuntimeException;

    List<ProductoAutocompledado> buscarAutocompletado(String term) throws RuntimeException;

    List<IProductoAlmacenList> listarProductosPorAlmacen(UUID uuid) throws RuntimeException;
}

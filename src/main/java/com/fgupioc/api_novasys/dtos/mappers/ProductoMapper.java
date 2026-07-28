package com.fgupioc.api_novasys.dtos.mappers;

import com.fgupioc.api_novasys.dtos.*;
import com.fgupioc.api_novasys.models.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 23/07/2026
 */
@Component
public class ProductoMapper {

    public ProductoRespDTO toRespDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        return new ProductoRespDTO(
                producto.getId(),
                producto.getUuid(),
                producto.getCode(),
                producto.getName(),
                producto.getDescription(),
                toCategoriaRespDTO(producto.getCategoria()),
                toCategoriaRespDTO(producto.getSubcategoria()),
                toMarcaRespDTO(producto.getMarca()),
                toUnidadRespDTO(producto.getUnidad()),
                toProveedorRespDTO(producto.getProveedor()),
                toProductoImagenes(producto.getImagenes()),
                producto.getCodStatus()
        );
    }

    private List<ProductoImagenRespDTO> toProductoImagenes(List<ProductoImagen> imagenes) {
        if (imagenes == null) {
            return null;
        }
        return imagenes.stream()
                .map(imagen -> new ProductoImagenRespDTO(imagen.getUrl(), imagen.getEsPrincipal()))
                .toList();
    }

    public List<ProductoRespDTO> toRespDTOList(List<Producto> productos) {
        return productos.stream()
                .map(this::toRespDTO)
                .toList();
    }

    private CategoriaRespDTO toCategoriaRespDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new CategoriaRespDTO(
                categoria.getId(),
                categoria.getCode(),
                categoria.getName()
        );
    }

    private MarcaRespDTO toMarcaRespDTO(Marca marca) {
        if (marca == null) {
            return null;
        }
        return Marca.toRespDTO(marca);
    }

    private UnidadRespDTO toUnidadRespDTO(Unidad unidad) {
        if (unidad == null) {
            return null;
        }
        return Unidad.toRespDTO(unidad);
    }

    private ProveedorRespDTO toProveedorRespDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }
        return Proveedor.toRespDTO(proveedor);
    }
}
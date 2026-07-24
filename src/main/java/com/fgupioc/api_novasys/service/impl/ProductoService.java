package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.ProductoReqDTO;
import com.fgupioc.api_novasys.dtos.ProductoRespDTO;
import com.fgupioc.api_novasys.dtos.mappers.ProductoMapper;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.*;
import com.fgupioc.api_novasys.models.interfaces.ProductoAutocompledado;
import com.fgupioc.api_novasys.repository.*;
import com.fgupioc.api_novasys.service.IImagenStorageService;
import com.fgupioc.api_novasys.service.IProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 22/07/2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService implements IProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final UnidadRepository unidadRepository;
    private final ProveedorRepository proveedorRepository;
    private final IImagenStorageService imagenStorageService;
    private final ProductoMapper productoMapper;

    @Override
    public ProductoRespDTO buscarPorUuid(UUID uuid) throws RuntimeException {
        return productoRepository.findByUuid(uuid)
                .map(productoMapper::toRespDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + uuid));
    }

    @Override
    public List<ProductoRespDTO> listarProductos() throws RuntimeException {
        val productos = productoRepository.findAllByOrderByIdDesc();
        return productos.stream()
                .map(productoMapper::toRespDTO)
                .toList();
    }

    @Override
    @Transactional
    public Producto registrar(ProductoReqDTO request, List<MultipartFile> imagenes) throws RuntimeException {
        try {

            Categoria categoria = categoriaRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + request.categoryId()));

            Categoria subcategoria = categoriaRepository.findById(request.subcategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada: " + request.subcategoryId()));

            Marca marca = marcaRepository.findById(request.brandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada: " + request.brandId()));

            Unidad unidad = unidadRepository.findById(request.unitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada: " + request.unitId()));

            Proveedor proveedor = null;
            if (request.providerId() != null) {
                proveedor = proveedorRepository.findById(request.providerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + request.providerId()));
            }
            Integer count = productoRepository.countProductos();
            String codigo = categoria.getCode() + String.format("%06d", count + 1);
            Producto producto = Producto.builder()
                    .code(codigo)
                    .name(request.name())
                    .description(request.description())
                    .categoria(categoria)
                    .subcategoria(subcategoria)
                    .marca(marca)
                    .unidad(unidad)
                    .proveedor(proveedor)
                    .codStatus(request.codStatus())
                    .build();

            Producto guardado = productoRepository.save(producto);

            if (imagenes != null && !imagenes.isEmpty()) {
                List<ProductoImagen> productoImagenes = new ArrayList<>();
                for (int i = 0; i < imagenes.size(); i++) {
                    String url = imagenStorageService.guardar(imagenes.get(i), "productos");
                    productoImagenes.add(
                            ProductoImagen.builder()
                                    .url(url)
                                    .esPrincipal(i == 0)
                                    .producto(guardado)
                                    .build()
                    );
                }
                guardado.getImagenes().addAll(productoImagenes);
            }

            return productoRepository.save(guardado);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Producto actualizar(UUID uuid, ProductoReqDTO request, List<MultipartFile> imagenes) {

        Producto producto = productoRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + uuid));

        Categoria categoria = categoriaRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + request.categoryId()));

        Categoria subcategoria = categoriaRepository.findById(request.subcategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada: " + request.subcategoryId()));

        Marca marca = marcaRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada: " + request.brandId()));

        Unidad unidad = unidadRepository.findById(request.unitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada: " + request.unitId()));

        Proveedor proveedor = null;
        if (request.providerId() != null) {
            proveedor = proveedorRepository.findById(request.providerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado: " + request.providerId()));
        }

        producto.setName(request.name());
        producto.setDescription(request.description());
        producto.setCategoria(categoria);
        producto.setSubcategoria(subcategoria);
        producto.setMarca(marca);
        producto.setUnidad(unidad);
        producto.setProveedor(proveedor);
        producto.setCodStatus(request.codStatus());

        if (imagenes != null && !imagenes.isEmpty()) {
            reemplazarImagenes(producto, imagenes);
        }

        return productoRepository.save(producto);
    }

    private void reemplazarImagenes(Producto producto, List<MultipartFile> imagenesNuevas) {
        // 1. Borra los archivos físicos de las imágenes anteriores
        producto.getImagenes().forEach(img -> imagenStorageService.eliminar(img.getUrl()));

        // 2. Limpia la colección — con orphanRemoval=true, Hibernate borra los registros huérfanos en BD
        producto.getImagenes().clear();

        // 3. Guarda y asocia las nuevas imágenes
        for (int i = 0; i < imagenesNuevas.size(); i++) {
            String url = imagenStorageService.guardar(imagenesNuevas.get(i), "productos");
            producto.getImagenes().add(
                    ProductoImagen.builder()
                            .url(url)
                            .esPrincipal(i == 0)
                            .producto(producto)
                            .build()
            );
        }
    }

    @Override
    public void eliminar(UUID uuid) throws RuntimeException {
        Producto producto = productoRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + uuid));

        // Elimina las imágenes asociadas
        // producto.getImagenes().forEach(img -> imagenStorageService.eliminar(img.getUrl()));

        // Elimina el producto
        productoRepository.delete(producto);
    }

    @Override
    public List<ProductoAutocompledado> buscarAutocompletado(String term) throws RuntimeException {
        String normalizado = (term == null || term.isBlank()) ? null : term.trim();
        return productoRepository.buscarProductos(normalizado);
    }
}

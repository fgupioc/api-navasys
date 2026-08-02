package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.ProductoReqDTO;
import com.fgupioc.api_novasys.dtos.ProductoRespDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.models.interfaces.IProductoAlmacenList;
import com.fgupioc.api_novasys.service.IImagenStorageService;
import com.fgupioc.api_novasys.service.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 22/07/2026
 */

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final IProductoService productoService;
    private final IImagenStorageService imagenStorageService;

    @GetMapping("{uuid}")
    public ResponseEntity<?> obtenerProducto(@PathVariable UUID uuid) {
        ProductoRespDTO producto = productoService.buscarPorUuid(uuid);
        return ResponseEntity.ok(ApiResponse.success("Producto encontrado", producto));
    }

    @GetMapping
    public ResponseEntity<?> listarProductos() {
        List<?> productos = productoService.listarProductos();
        return ResponseEntity.ok(ApiResponse.success("Lista de productos", productos));
    }

    @GetMapping("/listar-almacen/{uuid}")
    public ResponseEntity<?> listarProductosPorAlmacen(@PathVariable UUID uuid) {
        List<IProductoAlmacenList> productos = productoService.listarProductosPorAlmacen(uuid);
        return ResponseEntity.ok(ApiResponse.success("Lista de productos", productos));
    }


    @PostMapping(value = "registrar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearProducto(
            @RequestPart("producto") ProductoReqDTO producto,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {

        productoService.registrar(producto, imagenes);
        return ResponseEntity.ok(ApiResponse.success("Producto creado exitosamente", null));
    }

    @PutMapping(value = "actualizar/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizarProducto(
            @PathVariable UUID uuid,
            @RequestPart("producto") ProductoReqDTO producto,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {

        productoService.actualizar(uuid, producto, imagenes);
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado exitosamente", null));
    }

    @GetMapping("/imagenes/{nombreArchivo:.+}")
    public ResponseEntity<Resource> verImagen(@PathVariable String nombreArchivo) {
        Resource recurso = imagenStorageService.cargar(nombreArchivo, "productos");

        String contentType = determinarContentType(recurso);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                .body(recurso);
    }

    private String determinarContentType(Resource recurso) {
        try {
            String contentType = Files.probeContentType(recurso.getFile().toPath());
            return contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        } catch (IOException e) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<?> eliminarProducto(@PathVariable UUID uuid) {
        productoService.eliminar(uuid);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente", null));
    }

    @DeleteMapping("desactivar-almacen/{id}")
    public ResponseEntity<?> desactivarAlmacen(@PathVariable Long id) {
        productoService.desactivarAlmacen(id);
        return ResponseEntity.ok(ApiResponse.success("Almacén desactivado exitosamente", null));
    }

    @PutMapping("activar-almacen/{id}")
    public ResponseEntity<?> activarAlmacen(@PathVariable Long id) {
        productoService.activarAlmacen(id);
        return ResponseEntity.ok(ApiResponse.success("Almacén activado exitosamente", null));
    }

    @GetMapping("buscar-autocompletado")
    public ResponseEntity<?> buscarAutocompletado(@RequestParam(required = false) String term) {
        return ResponseEntity.ok(ApiResponse.success(productoService.buscarAutocompletado(term)));
    }
}

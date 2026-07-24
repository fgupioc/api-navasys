package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.helpers.FileStorageException;
import com.fgupioc.api_novasys.helpers.InvalidFileException;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.service.IImagenStorageService;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 22/07/2026
 */

@Service
@Slf4j
public class ImagenStorageService implements IImagenStorageService {
    @Value("${app.storage.base-path}")
    private String basePath;

    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of("jpg", "jpeg", "png", "webp");
    private static final long TAMANO_MAXIMO_BYTES = 5 * 1024 * 1024; // 5 MB

    @Override
    public String guardar(MultipartFile archivo, String subcarpeta) {
        validar(archivo);

        try {
            String extension = obtenerExtension(archivo.getOriginalFilename());
            String nombreArchivo = UUID.randomUUID() + "." + extension;

            Path directorioDestino = Paths.get(basePath, subcarpeta);
            Files.createDirectories(directorioDestino);

            Path rutaCompleta = directorioDestino.resolve(nombreArchivo);
            System.out.printf("ruta " + rutaCompleta);
            archivo.transferTo(rutaCompleta.toFile());

            return nombreArchivo;

        } catch (IOException e) {
            log.error("Error al guardar archivo: {}", archivo.getOriginalFilename(), e);
            throw new FileStorageException("No se pudo guardar la imagen", e);

        }
    }

    @Override
    public void eliminar(String url) {
        if (url == null || url.isBlank()) return;

        try {
            String rutaRelativa = url;
            Path ruta = Paths.get(basePath, rutaRelativa);
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            // No relanzamos: fallar al borrar un archivo huérfano no debe tumbar la transacción
            log.warn("No se pudo eliminar la imagen: {}", url, e);
        }
    }

    private void validar(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new InvalidFileException("El archivo está vacío");
        }

        if (archivo.getSize() > TAMANO_MAXIMO_BYTES) {
            throw new InvalidFileException("El archivo excede el tamaño máximo de 5MB");
        }

        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!EXTENSIONES_PERMITIDAS.contains(extension.toLowerCase())) {
            throw new InvalidFileException("Formato no permitido. Use: " + EXTENSIONES_PERMITIDAS);
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("El archivo debe ser una imagen");
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            throw new InvalidFileException("El archivo no tiene una extensión válida");
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1);
    }

    @Override
    public Resource cargar(String nombreArchivo, String subcarpeta) {
        try {
            Path ruta = Paths.get(basePath, subcarpeta).resolve(nombreArchivo).normalize();

            // Seguridad: evita path traversal (ej. ../../etc/passwd)
            Path directorioBase = Paths.get(basePath, subcarpeta).normalize();
            if (!ruta.startsWith(directorioBase)) {
                throw new InvalidFileException("Ruta de archivo inválida");
            }

            Resource recurso = new UrlResource(ruta.toUri());

            if (!recurso.exists() || !recurso.isReadable()) {
                throw new ResourceNotFoundException("Imagen no encontrada: " + nombreArchivo);
            }

            return recurso;

        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Imagen no encontrada: " + nombreArchivo);
        }
    }
}

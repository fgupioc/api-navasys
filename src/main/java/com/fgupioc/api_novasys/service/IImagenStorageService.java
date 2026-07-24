package com.fgupioc.api_novasys.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IImagenStorageService {
    String guardar(MultipartFile archivo, String subcarpeta) throws RuntimeException;

    void eliminar(String url) throws RuntimeException;

    Resource cargar(String nombreArchivo, String subcarpeta);
}

package com.fgupioc.api_novasys.service;

import com.fgupioc.api_novasys.dtos.CategoriaParentRespDTO;
import com.fgupioc.api_novasys.dtos.CategoriaRespDTO;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaParentRespDTO> listarCategoriasPadres() throws RuntimeException;

    List<CategoriaRespDTO> listarCategorias() throws RuntimeException;

    void registrar(CategoriaRespDTO categoriaDTO) throws RuntimeException;

    void actualizar(Long id, CategoriaRespDTO categoriaDTO) throws RuntimeException;

    void eliminar(Long categoryId) throws RuntimeException;
}

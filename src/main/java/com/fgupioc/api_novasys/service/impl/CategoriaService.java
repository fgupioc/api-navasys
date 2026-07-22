package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.CategoriaParentRespDTO;
import com.fgupioc.api_novasys.dtos.CategoriaRespDTO;
import com.fgupioc.api_novasys.helpers.ResourceNotFoundException;
import com.fgupioc.api_novasys.models.Categoria;
import com.fgupioc.api_novasys.repository.CategoriaRepository;
import com.fgupioc.api_novasys.service.ICategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 21/07/2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService implements ICategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaParentRespDTO> listarCategoriasPadres() throws RuntimeException {
        List<Categoria> categorias = categoriaRepository.findAllPadresByOrderByIdDesc();
        return categorias.stream().map(categoria -> new CategoriaParentRespDTO(
                categoria.getId(),
                categoria.getCode(),
                categoria.getName()
        )).toList();
    }

    @Override
    public List<CategoriaRespDTO> listarCategorias() throws RuntimeException {
        List<Categoria> categorias = categoriaRepository.findAllByOrderByIdDesc();
        return categorias.stream().map(categoria -> new CategoriaRespDTO(
                categoria.getId(),
                categoria.getCode(),
                categoria.getName(),
                categoria.getCodStatus(),
                categoria.getParent() != null ? new CategoriaParentRespDTO(
                        categoria.getParent().getId(),
                        categoria.getParent().getCode(),
                        categoria.getParent().getName()
                ) : null
        )).toList();
    }

    @Override
    public void registrar(CategoriaRespDTO categoriaDTO) throws RuntimeException {
        try {
            categoriaRepository.findByCode(categoriaDTO.code()).ifPresent(_ -> {
                throw new RuntimeException("Ya existe una categoría con el código: " + categoriaDTO.code());
            });
            Categoria categoria = new Categoria();
            categoria.setCode(categoriaDTO.code());
            categoria.setName(categoriaDTO.name());

            if (categoriaDTO.parent() != null) {
                Categoria parent = categoriaRepository.findById(categoriaDTO.parent().id())
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría padre no encontrada"));
                categoria.setParent(parent);
            }
            categoriaRepository.save(categoria);
        } catch (RuntimeException e) {
            log.error("Error al registrar la categoría: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void actualizar(Long id, CategoriaRespDTO categoriaDTO) throws RuntimeException {
        try {
            categoriaRepository.findByCodeOther(categoriaDTO.code(), id).ifPresent(_ -> {
                throw new RuntimeException("Ya existe una categoría con el código: " + categoriaDTO.code());
            });

            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            categoria.setCode(categoriaDTO.code());
            categoria.setName(categoriaDTO.name());
            categoria.setCodStatus(categoriaDTO.codStatus());

            if (categoriaDTO.parent() != null) {
                Categoria parent = categoriaRepository.findById(categoriaDTO.parent().id())
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría padre no encontrada"));
                categoria.setParent(parent);
            } else {
                categoria.setParent(null);
            }
            categoriaRepository.save(categoria);
        } catch (RuntimeException e) {
            log.error("Error al actualizar la categoría: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void eliminar(Long categoryId) throws RuntimeException {
        try {
            Categoria categoria = categoriaRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            categoriaRepository.delete(categoria);
        } catch (RuntimeException e) {
            log.error("Error al eliminar la categoría: {}", e.getMessage());
            throw e;
        }
    }
}

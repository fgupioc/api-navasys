package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.CategoriaRespDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.ICategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 21/07/2026
 */

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final ICategoriaService categoriaService;

    @GetMapping("/padres")
    public ResponseEntity<?> findAllPadres() {
        return ResponseEntity.ok(ApiResponse.success(categoriaService.listarCategoriasPadres()));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(ApiResponse.success(categoriaService.listarCategorias()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoriaRespDTO categoriaDTO) {
        categoriaService.registrar(categoriaDTO);
        return ResponseEntity.ok(ApiResponse.success("Categoría creada exitosamente", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoriaRespDTO categoriaDTO) {
        categoriaService.actualizar(id, categoriaDTO);
        return ResponseEntity.ok(ApiResponse.success("Categoría actualizada exitosamente", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Categoría eliminada exitosamente", null));
    }
}

package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.MarcaReqDTO;
import com.fgupioc.api_novasys.dtos.MarcaRespDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.IMarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 20/07/2026
 */

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final IMarcaService unidadService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MarcaRespDTO>>> findAll() {
        List<MarcaRespDTO> marcas = unidadService.findAll();
        return ResponseEntity.ok(ApiResponse.success(marcas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MarcaRespDTO>> findById(@PathVariable Long id) {
        MarcaRespDTO unidad = unidadService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(unidad));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MarcaRespDTO>> create(@Valid @RequestBody MarcaReqDTO dto) {
        MarcaRespDTO created = unidadService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Marca creada correctamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MarcaRespDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody MarcaReqDTO dto) {
        MarcaRespDTO updated = unidadService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Marca actualizada correctamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        unidadService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Marca eliminada correctamente", null));
    }
}

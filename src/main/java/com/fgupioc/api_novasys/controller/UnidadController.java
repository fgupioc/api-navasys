package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.UnidadReqDTO;
import com.fgupioc.api_novasys.dtos.UnidadRespDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.IUnidadService;
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
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
public class UnidadController {

    private final IUnidadService unidadService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnidadRespDTO>>> findAll() {
        List<UnidadRespDTO> unidades = unidadService.findAll();
        return ResponseEntity.ok(ApiResponse.success(unidades));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnidadRespDTO>> findById(@PathVariable Long id) {
        UnidadRespDTO unidad = unidadService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(unidad));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UnidadRespDTO>> create(@Valid @RequestBody UnidadReqDTO dto) {
        UnidadRespDTO created = unidadService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Unidad creada correctamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnidadRespDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UnidadReqDTO dto) {
        UnidadRespDTO updated = unidadService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Unidad actualizada correctamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        unidadService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Unidad eliminada correctamente", null));
    }
}

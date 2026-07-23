package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.ProveedorReqDTO;
import com.fgupioc.api_novasys.dtos.ProveedorRespDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.IProveedorService;
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
 * @since 22/07/2026
 */

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController {
    private final IProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProveedorRespDTO>>> findAll() {
        List<ProveedorRespDTO> marcas = proveedorService.findAll();
        return ResponseEntity.ok(ApiResponse.success(marcas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProveedorRespDTO>> findById(@PathVariable Long id) {
        ProveedorRespDTO proveedor = proveedorService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(proveedor));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProveedorRespDTO>> create(@Valid @RequestBody ProveedorReqDTO dto) {
        ProveedorRespDTO created = proveedorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Proveedor creada correctamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProveedorRespDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorReqDTO dto) {
        ProveedorRespDTO updated = proveedorService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Proveedor actualizada correctamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        proveedorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Proveedor eliminada correctamente", null));
    }
}

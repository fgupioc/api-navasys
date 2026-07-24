package com.fgupioc.api_novasys.controller;

import com.fgupioc.api_novasys.dtos.ClienteReqDTO;
import com.fgupioc.api_novasys.dtos.ClienteRespDTO;
import com.fgupioc.api_novasys.helpers.ApiResponse;
import com.fgupioc.api_novasys.service.IClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final IClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteRespDTO>>> findAll() {
        List<ClienteRespDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(ApiResponse.success(clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteRespDTO>> findById(@PathVariable Long id) {
        ClienteRespDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(cliente));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteRespDTO>> create(@Valid @RequestBody ClienteReqDTO dto) {
        ClienteRespDTO created = clienteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente creada correctamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteRespDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteReqDTO dto) {
        ClienteRespDTO updated = clienteService.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cliente actualizada correctamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente eliminada correctamente", null));
    }
}

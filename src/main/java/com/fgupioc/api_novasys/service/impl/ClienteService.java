package com.fgupioc.api_novasys.service.impl;

import com.fgupioc.api_novasys.dtos.ClienteReqDTO;
import com.fgupioc.api_novasys.dtos.ClienteRespDTO;
import com.fgupioc.api_novasys.models.Cliente;
import com.fgupioc.api_novasys.repository.ClienteRepository;
import com.fgupioc.api_novasys.service.IClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fgupioc - FRANZ
 * @version 1.0
 * @project api-novasys
 * @since 23/07/2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService implements IClienteService {
    private final ClienteRepository clienteRepository;


    @Override
    public List<ClienteRespDTO> findAll() throws RuntimeException {
        return clienteRepository.findAllByOrderByIdDesc().stream()
                .map(cliente -> new ClienteRespDTO(
                        cliente.getId(),
                        cliente.getDocumentType(),
                        cliente.getDocumentNumber(),
                        cliente.getFirstName(),
                        cliente.getLastName(),
                        cliente.getPhone(),
                        cliente.getCodStatus()
                ))
                .toList();
    }

    @Override
    public ClienteRespDTO findById(Long id) throws RuntimeException {
        return clienteRepository.findById(id)
                .map(cliente -> new ClienteRespDTO(
                        cliente.getId(),
                        cliente.getDocumentType(),
                        cliente.getDocumentNumber(),
                        cliente.getFirstName(),
                        cliente.getLastName(),
                        cliente.getPhone(),
                        cliente.getCodStatus()
                ))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    @Override
    public ClienteRespDTO create(ClienteReqDTO dto) throws RuntimeException {
        clienteRepository.existsByDocumentNumber(dto.documentNumber()).ifPresent(cliente -> {
            throw new RuntimeException("El cliente con número de documento " + dto.documentNumber() + " ya existe.");
        });
        Cliente cliente = new Cliente();
        cliente.setDocumentType(dto.documentType());
        cliente.setDocumentNumber(dto.documentNumber());
        cliente.setFirstName(dto.firstName());
        cliente.setLastName(dto.lastName());
        cliente.setPhone(dto.phone());
        clienteRepository.save(cliente);
        return new ClienteRespDTO(
                cliente.getId(),
                cliente.getDocumentType(),
                cliente.getDocumentNumber(),
                cliente.getFirstName(),
                cliente.getLastName(),
                cliente.getPhone(),
                cliente.getCodStatus()
        );
    }

    @Override
    public ClienteRespDTO update(Long id, ClienteReqDTO dto) throws RuntimeException {
        clienteRepository.existsByDocumentNumberDistinct(id, dto.documentNumber()).ifPresent(cliente -> {
            throw new RuntimeException("El cliente con número de documento " + dto.documentNumber() + " ya existe.");
        });
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        cliente.setDocumentType(dto.documentType());
        cliente.setDocumentNumber(dto.documentNumber());
        cliente.setFirstName(dto.firstName());
        cliente.setLastName(dto.lastName());
        cliente.setPhone(dto.phone());
        cliente.setCodStatus(dto.codStatus());
        clienteRepository.save(cliente);
        return new ClienteRespDTO(
                cliente.getId(),
                cliente.getDocumentType(),
                cliente.getDocumentNumber(),
                cliente.getFirstName(),
                cliente.getLastName(),
                cliente.getPhone(),
                cliente.getCodStatus()
        );
    }

    @Override
    public void delete(Long id) throws RuntimeException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        clienteRepository.delete(cliente);
    }
}

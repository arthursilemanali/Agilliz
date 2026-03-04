package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.despesa.DespesaRequest;
import agiliz.projetoAgiliz.dto.despesa.DespesaResponse;
import agiliz.projetoAgiliz.services.DespesaService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/despesas")
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
public class DespesaController {

    private final DespesaService service;

    @PostMapping
    public ResponseEntity<MensageriaService<DespesaResponse>> cadastrar(
        @RequestBody @Valid DespesaRequest dto
    ) {
        return status(HttpStatus.CREATED)
            .body(
                new MensageriaService<DespesaResponse>()
                    .mensagemCliente("Despesa cadastrada com sucesso")
                    .data(service.inserir(dto))
                    .status(201)
            );
    }

    @GetMapping
    @Cacheable
    public ResponseEntity<MensageriaService<List<DespesaResponse>>> getAll() {
        var despesasResponse = service.getAllResponse();

        if(despesasResponse.isEmpty()) status(HttpStatus.NO_CONTENT).build();

        return status(HttpStatus.OK)
                .body(
                    new MensageriaService<List<DespesaResponse>>()
                        .mensagemCliente("Despesas: ")
                        .data(despesasResponse)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<DespesaResponse>> getPorId(@PathVariable UUID id) {
        return status(200)
            .body(
                new MensageriaService<DespesaResponse>()
                    .mensagemCliente("Despesa por id")
                    .data(service.getResponsePorId(id))
                    .status(200)
            );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<DespesaResponse>> alterar(
        @PathVariable UUID id,
        @RequestBody @Valid DespesaRequest dto
    ) {
        return status(HttpStatus.OK).body(
            new MensageriaService<DespesaResponse>()
                .mensagemCliente("Alterado com sucesso")
                .data(service.alterar(id, dto))
                .status(200)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletarPorId(UUID.fromString(id));
        return noContent().build();
    }
}

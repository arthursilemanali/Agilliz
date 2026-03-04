package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.setor.AtualizacaoLider;
import agiliz.projetoAgiliz.dto.setor.SetorRequest;
import agiliz.projetoAgiliz.dto.setor.SetorResponse;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.SetorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/setores")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SetorController {
    private final SetorService service;

    @PostMapping
    public ResponseEntity<MensageriaService<SetorResponse>> cadastrar(@RequestBody @Valid SetorRequest dto) {
        return status(HttpStatus.CREATED)
                .body(
                        new MensageriaService<SetorResponse>()
                                .status(201)
                                .data(service.inserir(dto))
                                .mensagemCliente("Setor cadastrado com sucesso")
                );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<SetorResponse>>> listar() {
        List<SetorResponse> setores = service.getAll();

        if(setores.isEmpty()) {
            return noContent().build();
        }

        return ok(
                new MensageriaService<List<SetorResponse>>()
                    .status(200)
                    .data(setores)
                    .mensagemCliente("Setores:")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<SetorResponse>> buscar(@PathVariable UUID id) {
        return ok(
                new MensageriaService<SetorResponse>()
                        .status(200)
                        .data(service.getResponsePorId(id))
                        .mensagemCliente("Setor:")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<SetorResponse>> alterar(
            @PathVariable UUID id,
            @RequestBody @Valid SetorRequest dto
    ) {
        return ok(
                new MensageriaService<SetorResponse>()
                        .status(200)
                        .data(service.alterar(dto, id))
                        .mensagemCliente("Setor alterado com sucesso")
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> alterarLider(@PathVariable UUID id, @RequestBody @Valid AtualizacaoLider dto) {
        service.alterarLiderSetor(id, dto.fkLider());
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.deletar(id);
        return noContent().build();
    }
}

package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.membroSetor.AlteracaoSetor;
import agiliz.projetoAgiliz.dto.membroSetor.MembroSetorRequest;
import agiliz.projetoAgiliz.dto.membroSetor.MembroSetorResponse;
import agiliz.projetoAgiliz.services.MembroSetorService;
import agiliz.projetoAgiliz.services.MensageriaService;
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
@RequestMapping("/membros-setor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class MembroSetorController {
    private final MembroSetorService service;

    @PostMapping
    public ResponseEntity<MensageriaService<MembroSetorResponse>> cadastrar(
            @RequestBody @Valid MembroSetorRequest dto
    ) {
        return status(HttpStatus.CREATED)
                .body(
                        new MensageriaService<MembroSetorResponse>()
                                .status(201)
                                .data(service.inserir(dto))
                                .mensagemCliente("Membro cadastrado com sucesso")
                );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<MembroSetorResponse>>> listar() {
        List<MembroSetorResponse> membros = service.getAll();

        if(membros.isEmpty()) {
            return noContent().build();
        }

        return ok(
                new MensageriaService<List<MembroSetorResponse>>()
                        .status(200)
                        .data(membros)
                        .mensagemCliente("Membros:")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<MembroSetorResponse>> listarPorId(@PathVariable UUID id) {
        return ok(
                new MensageriaService<MembroSetorResponse>()
                        .status(200)
                        .data(service.getResponsePorId(id))
                        .mensagemCliente("Membro:")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<MembroSetorResponse>> alterar(
            @PathVariable UUID id,
            @RequestBody @Valid MembroSetorRequest dto
    ) {
        return ok(
                new MensageriaService<MembroSetorResponse>()
                        .status(200)
                        .data(service.alterar(dto, id))
                        .mensagemCliente("Membro alterado com sucesso")
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> alterarSetor(@PathVariable UUID id, @RequestBody @Valid AlteracaoSetor dto) {
        service.alterarSetor(id, dto.fkSetor());
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        service.deletar(id);
        return noContent().build();
    }
}

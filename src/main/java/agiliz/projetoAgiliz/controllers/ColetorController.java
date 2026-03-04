package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.coletor.ColetorPatchRequest;
import agiliz.projetoAgiliz.dto.coletor.ColetorRequest;
import agiliz.projetoAgiliz.dto.coletor.ColetorResponse;
import agiliz.projetoAgiliz.services.ColetorService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/coletores")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
public class ColetorController {
    @Autowired
    private ColetorService service;

    @PostMapping
    public ResponseEntity<MensageriaService<ColetorResponse>> cadastrar(
        @RequestBody @Valid ColetorRequest dto
    ) {
        return ok(
            new MensageriaService<ColetorResponse>()
                .mensagemCliente("Coletor cadastrado com sucesso:")
                .data(service.cadastrar(dto))
                .status(200)
        );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<ColetorResponse>>> getColetores(
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "5") int tamanho
    ) {
        var coletores = service.getColetores(pagina, tamanho);

        if(coletores.isEmpty()) return noContent().build();

        return ok(
            new MensageriaService<Page<ColetorResponse>>()
                .mensagemCliente("Coletores:")
                .data(coletores)
                .status(200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<ColetorResponse>> getColetorPorId(@PathVariable UUID id) {
        return ok(
            new MensageriaService<ColetorResponse>()
                .mensagemCliente("Coletor:")
                .data(service.getResponsePorId(id))
                .status(200)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> alterar(
        @PathVariable UUID id,
        @RequestBody @Valid ColetorPatchRequest dto
    ){
        service.alterarQuantidadePacotes(id, dto);
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

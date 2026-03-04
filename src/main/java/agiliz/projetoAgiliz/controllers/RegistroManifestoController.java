package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.registroManifesto.RegistroManifestoPatchRequest;
import agiliz.projetoAgiliz.dto.registroManifesto.RegistroManifestoRequest;
import agiliz.projetoAgiliz.dto.registroManifesto.RegistroManifestoResponse;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.RegistroManifestoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/registros-manifesto")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RegistroManifestoController {

    @Autowired
    private RegistroManifestoService service;

    @PostMapping
    public ResponseEntity<MensageriaService<RegistroManifestoResponse>> cadastrar(
        @RequestBody @Valid RegistroManifestoRequest dto
    ) {
        return status(HttpStatus.CREATED)
            .body(
                new MensageriaService<RegistroManifestoResponse>()
                    .data(service.cadastrar(dto))
                    .status(201)
                    .mensagemCliente("Registro manifesto cadastrado com sucesso")
            );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<RegistroManifestoResponse>>> getRegistrosManifesto(
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "5") int tamanho
    ) {
        var registros = service.getRegistrosManifestosResponsePaginados(pagina, tamanho);

        if(registros.isEmpty()) return noContent().build();

        return ok(
                new MensageriaService<Page<RegistroManifestoResponse>>()
                        .data(registros)
                        .status(200)
                        .mensagemCliente("Registros de manifesto:")
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<RegistroManifestoResponse>> getRegistroPorId(
        @PathVariable UUID id
    ) {
        return ok(
                new MensageriaService<RegistroManifestoResponse>()
                        .data(service.getRegistroManifestoResponsePorId(id))
                        .status(201)
                        .mensagemCliente("Pacote cadastrado com sucesso")
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> alterarManifesto(
        @PathVariable UUID id,
        @RequestBody @Valid RegistroManifestoPatchRequest dto
    ) {
        service.alterarManifesto(id, dto.fkManifesto());
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.manifesto.ManifestoPatchRequest;
import agiliz.projetoAgiliz.dto.manifesto.ManifestoRequest;
import agiliz.projetoAgiliz.dto.manifesto.ManifestoResponse;
import agiliz.projetoAgiliz.services.ManifestoService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/manifestos")
public class ManifestoController {

    @Autowired
    private ManifestoService service;

    @PostMapping
    public ResponseEntity<MensageriaService<List<ManifestoResponse>>> cadastrar(
            @RequestBody @Valid List<ManifestoRequest> dto
    ) {
        return status(HttpStatus.CREATED)
                .body(
                        new MensageriaService<List<ManifestoResponse>>()
                                .status(201)
                                .data(service.cadastrar(dto))
                                .mensagemCliente("Manifestos criados:")
                );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<ManifestoResponse>>> getManifestos() {
        var manifestos = service.getManifestosResponse();

        if (manifestos.isEmpty()) return noContent().build();

        return ok(
                new MensageriaService<List<ManifestoResponse>>()
                        .status(200)
                        .data(manifestos)
                        .mensagemCliente("Manifestos:")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<ManifestoResponse>> getManifestoPorId(@PathVariable UUID id) {
        return ok(
                new MensageriaService<ManifestoResponse>()
                        .mensagemCliente("Manifesto:")
                        .data(service.getManifestoResponsePorId(id))
                        .status(200)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable UUID id,
            @RequestBody @Valid ManifestoPatchRequest dto
    ) {
        service.alterarStatus(id, dto.status());
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

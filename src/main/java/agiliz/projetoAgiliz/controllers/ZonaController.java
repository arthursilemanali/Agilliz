package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.zona.ZonaRequest;
import agiliz.projetoAgiliz.dto.zona.ZonaResponse;
import agiliz.projetoAgiliz.dto.zona.ZonaResponsePut;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.ZonaService;
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
@RequestMapping("/zonas")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_ADMIN'")
public class ZonaController {
    @Autowired
    private ZonaService service;
    
    @PostMapping
    public ResponseEntity<MensageriaService<ZonaResponse>> cadastrar(
        @RequestBody @Valid ZonaRequest dto
    ) {
        return status(HttpStatus.CREATED)
            .body(
                new MensageriaService<ZonaResponse>()
                    .data(service.cadastrar(dto))
                    .mensagemCliente("Zona cadastrada com sucesso")
                    .status(201)
            );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<ZonaResponsePut>>> getZonasPaginadas(
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "5") int tamanho
    ) {
        var zonas = service.getZonasPaginadas(pagina, tamanho);

        if(zonas.isEmpty()) return noContent().build();

        return ok(
            new MensageriaService<Page<ZonaResponsePut>>()
                .data(zonas)
                .mensagemCliente("Zonas:")
                .status(200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<ZonaResponse>> listarPorId(@PathVariable UUID id){
        return ok(
            new MensageriaService<ZonaResponse>()
                .data(service.getResposePorId(id))
                .mensagemCliente("Zona:")
                .status(200)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<ZonaResponse>> alterar(
        @PathVariable UUID id,
        @RequestBody @Valid ZonaRequest dto
    ) {
        return ok(
            new MensageriaService<ZonaResponse>()
                .mensagemCliente("Zona atualizada com sucesso")
                .data(service.alterar(id, dto))
                .status(200)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

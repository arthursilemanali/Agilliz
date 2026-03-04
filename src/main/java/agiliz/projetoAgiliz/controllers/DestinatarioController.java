package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.destinatario.DestinatarioRequest;
import agiliz.projetoAgiliz.dto.destinatario.DestinatarioResponse;
import agiliz.projetoAgiliz.services.DestinatarioService;
import agiliz.projetoAgiliz.services.MensageriaService;
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
@RequestMapping("/destinatarios")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class DestinatarioController {
    @Autowired
    private DestinatarioService service;

    @GetMapping
    public ResponseEntity<MensageriaService<Page<DestinatarioResponse>>> listDestinatarios(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int size
    ) {
        var destinatarios = service.getDestinatariosPaginados(pagina, size);

        return destinatarios.isEmpty()
            ? noContent().build()
            : ok(
                new MensageriaService<Page<DestinatarioResponse>>()
                        .mensagemCliente("Destinatários:")
                        .data(destinatarios)
                        .status(200)
            );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<DestinatarioResponse>> getDestinatarioPorId(
        @PathVariable UUID id
    ) {
        return ok(
            new MensageriaService<DestinatarioResponse>()
                .mensagemCliente("Destinatário")
                .data(service.getResponsePorId(id))
                .status(200)
        );
    }

    @PostMapping
    public ResponseEntity<MensageriaService<DestinatarioResponse>> cadastrarDestinatario(
        @RequestBody @Valid DestinatarioRequest dto
    ) {
        return status(HttpStatus.CREATED).body(
            new MensageriaService<DestinatarioResponse>()
                .data(service.cadastrar(dto))
                .mensagemCliente("Destinátario")
                .status(201)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<DestinatarioResponse>> alterarDestinatario(
        @PathVariable UUID id,
        @RequestBody @Valid DestinatarioRequest dto
    ){
        return ok(
          new MensageriaService<DestinatarioResponse>()
              .data(service.alterar(id, dto))
              .mensagemCliente("Destinatário alterado com sucesso")
              .status(200)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensageriaService<Void>> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

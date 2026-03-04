package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.comprovanteEntrega.ComprovanteEntregaRequest;
import agiliz.projetoAgiliz.dto.comprovanteEntrega.ComprovanteEntregaResponse;
import agiliz.projetoAgiliz.services.ComprovanteEntregaService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/comprovantes-entrega")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class ComprovanteEntregaController {

    private final ComprovanteEntregaService service;

    @PostMapping
    public ResponseEntity<MensageriaService<ComprovanteEntregaResponse>> cadastrar(
        @RequestBody @Valid ComprovanteEntregaRequest dto
    ) {
        return ok(
                new MensageriaService<ComprovanteEntregaResponse>()
                        .mensagemCliente("Comprovante cadastrado")
                        .data(service.cadastrar(dto))
                        .status(200)
                );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<ComprovanteEntregaResponse>>> getComprovantesPaginados(
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "5") int tamanho
    ) {
        var comprovantes = service.getComprovantesResponsePaginados(pagina, tamanho);

        if(comprovantes.isEmpty()) return noContent().build();

        return ok(
            new MensageriaService<Page<ComprovanteEntregaResponse>>()
                .mensagemCliente("Comprovantes:")
                .data(comprovantes)
                .status(200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<ComprovanteEntregaResponse>> getPorId(@PathVariable UUID id) {
        return ok(
            new MensageriaService<ComprovanteEntregaResponse>()
                .mensagemCliente("Comprovante:")
                .data(service.getResponsePorId(id))
                .status(200)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<ComprovanteEntregaResponse>> alterar(
        @PathVariable UUID id,
        @RequestBody @Valid ComprovanteEntregaRequest dto
    ) {
        return ok(
            new MensageriaService<ComprovanteEntregaResponse>()
                .mensagemCliente("Comprovante alterado com sucesso")
                .data(service.alterar(dto, id))
                .status(200)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return status(HttpStatus.NO_CONTENT).build();
    }
}

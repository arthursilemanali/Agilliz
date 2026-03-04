package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaRequest;
import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaResponse;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.PrecificacaoZonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/precificacoes-zona")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
public class PrecificacaoZonaController {
    @Autowired
    private PrecificacaoZonaService service;

    @PostMapping
    public ResponseEntity<MensageriaService<PrecificacaoZonaResponse>> cadastrar(
        @RequestBody @Valid PrecificacaoZonaRequest dto
    ) {
      return ok(
              new MensageriaService<PrecificacaoZonaResponse>()
                  .mensagemCliente("Precificação de zona cadastrada com sucesso")
                  .data(service.cadastrar(dto))
                  .status(201)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<PrecificacaoZonaResponse>> getPrecificacaoZonaPorId(
        @PathVariable UUID id
    ) {
        return ok(
                new MensageriaService<PrecificacaoZonaResponse>()
                    .mensagemCliente("Colaborador por id")
                    .data(service.getResponsePorId(id))
                    .status(200)
        );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<PrecificacaoZonaResponse>>> getPrecificacoesZona(
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "5") int tamanho
    ) {
        var precificacoes = service.getPrecificacoesPaginadas(pagina, tamanho);

        if(precificacoes.isEmpty()) return noContent().build();

        return ok(
            new MensageriaService<Page<PrecificacaoZonaResponse>>()
                    .mensagemCliente("Precificações Zona")
                    .data(precificacoes)
                    .status(200)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<PrecificacaoZonaResponse>> alterar(
        @PathVariable UUID id,
        @RequestBody @Valid PrecificacaoZonaRequest dto
    ) {
        return ok(
                new MensageriaService<PrecificacaoZonaResponse>()
                    .mensagemCliente("Precificação de zona alterada com sucesso")
                    .data(service.alterar(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

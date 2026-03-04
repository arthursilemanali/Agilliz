package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.transacao.TransacaoRequest;
import agiliz.projetoAgiliz.dto.transacao.TransacaoResponse;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {
    private final TransacaoService service;

    @PostMapping
    public ResponseEntity<TransacaoResponse> cadastrar(@RequestBody @Valid TransacaoRequest dto) {
        return status(HttpStatus.CREATED).body(service.inserir(dto));
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<TransacaoResponse>>> getTransacoes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho
    ) {
        var transacoes = service.getTransacoesPaginadas(pagina, tamanho);

        if(transacoes.isEmpty()) {
            return noContent().build();
        }

        return ok(
                new MensageriaService<Page<TransacaoResponse>>()
                        .mensagemCliente("Transações:")
                        .data(transacoes)
                        .status(200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<TransacaoResponse>> getPorId(@PathVariable UUID id) {
        return ok(
                new MensageriaService<TransacaoResponse>()
                        .mensagemCliente("Transação encontrada:")
                        .data(service.getResponsePorId(id))
                        .status(200)
        );
    }

    @GetMapping("/colaborador/{idColaborador}")
    public ResponseEntity<MensageriaService<Page<TransacaoResponse>>> getTransacoesColaborador(
            @PathVariable UUID idColaborador,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho
    ) {
        Page<TransacaoResponse> transacoes = service.getTransacoesColaborador(idColaborador, pagina, tamanho);

        if(transacoes.isEmpty()) {
            return noContent().build();
        }

        return ok(
            new MensageriaService<Page<TransacaoResponse>>()
                    .mensagemCliente("Transações encontradas:")
                    .data(transacoes)
                    .status(200)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

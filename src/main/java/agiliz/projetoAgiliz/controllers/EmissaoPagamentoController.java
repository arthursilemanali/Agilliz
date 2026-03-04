package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.OS.PagamentoOsRequest;
import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoResponse;
import agiliz.projetoAgiliz.services.EmissaoPagamentoService;
import agiliz.projetoAgiliz.services.MensageriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/emissoes-pagamento")
@CrossOrigin
public class EmissaoPagamentoController {
    @Autowired
    private EmissaoPagamentoService service;

    @PostMapping
    public ResponseEntity<Void> emitirPagamento(@RequestBody PagamentoOsRequest pagamentoOsRequest) {
        service.cadastrar(pagamentoOsRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<EmissaoPagamentoResponse>>> getEmissoes() {
        var emissoes = service.getEmissoesResponse();

        if (emissoes.isEmpty()) return noContent().build();

        return ok(
                new MensageriaService<List<EmissaoPagamentoResponse>>()
                        .data(emissoes)
                        .mensagemCliente("Emissões de pagamento:")
                        .status(200)
        );
    }
}

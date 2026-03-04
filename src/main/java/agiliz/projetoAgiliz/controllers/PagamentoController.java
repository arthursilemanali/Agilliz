package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.pagamento.PagamentoRequest;
import agiliz.projetoAgiliz.dto.pagamento.PagamentoResponse;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.PagamentoService;
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
@RequestMapping("/pagamentos")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
public class PagamentoController {
    @Autowired
    private PagamentoService service;

    @PostMapping
    public ResponseEntity<MensageriaService<PagamentoResponse>> cadastrar(
            @RequestBody @Valid PagamentoRequest dto
    ){
        return status(HttpStatus.CREATED)
                .body(
                        new MensageriaService<PagamentoResponse>()
                                .mensagemCliente("Inserido com sucesso")
                                .data(service.cadastrar(dto))
                                .status(201)
                );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<PagamentoResponse>>> getPagamentosPaginados(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho
    ) {
        var pagamentos = service.getPagamentosPaginados(pagina, tamanho);

        if(pagamentos.isEmpty()) return noContent().build();

        return ok(
                new MensageriaService<Page<PagamentoResponse>>()
                        .mensagemCliente("Pagamentos:")
                        .data(pagamentos)
                        .status(200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<PagamentoResponse>> getPorId(@PathVariable UUID id){
        return ok(
                new MensageriaService<PagamentoResponse>()
                        .mensagemCliente("Pagamento:")
                        .data(service.getResponsePorId(id))
                        .status(200)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}
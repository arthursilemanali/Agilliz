package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.carteira.*;
import agiliz.projetoAgiliz.services.CarteiraService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/carteiras")
@PreAuthorize("hasRole('ROLE_MOTOBOY')")
public class CarteiraController {

    @Autowired
    private CarteiraService service;

    @PostMapping
    public ResponseEntity<MensageriaService<CarteiraResponse>> cadastro(
            @RequestBody @Valid CarteiraPostRequest dto
    ) {
        return status(HttpStatus.CREATED)
                .body(
                        new MensageriaService<CarteiraResponse>()
                                .data(service.cadastro(dto))
                                .mensagemCliente("Carteira cadastrada com sucesso")
                                .status(201)
                );
    }

    @GetMapping
    public ResponseEntity<List<CarteiraResponse>> getCarteiras() {
        var dados = service.getCarteiraResponse();

        if (dados.isEmpty()) return noContent().build();

        return ok(
                dados
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<CarteiraResponse>> getCarteiraPorId(
            @PathVariable UUID id
    ) {
        return ok(
                new MensageriaService<CarteiraResponse>()
                        .data(service.getCarteiraResponsePorId(id))
                        .mensagemCliente("Carteira:")
                        .status(200)
        );
    }

    @GetMapping("/carteira-colaborador/{fkColaborador}")
    public ResponseEntity<MensageriaService<CarteiraResponse>> getCarteiraPorFkColaborador(
            @PathVariable UUID fkColaborador
    ) {
        return ok(
                new MensageriaService<CarteiraResponse>()
                        .data(service.getCarteiraPorColaborador(fkColaborador))
                        .mensagemCliente("Carteira:")
                        .status(200)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<CarteiraResponse>> alterar(
            @PathVariable UUID id,
            @RequestBody @Valid CarteiraPutRequest dto
    ) {
        return ok(
                new MensageriaService<CarteiraResponse>()
                        .data(service.alterar(id, dto))
                        .mensagemCliente("Carteira alterada com sucesso")
                        .status(200)
        );
    }

    @GetMapping("/dados/{idColaborador}")
    public ResponseEntity<MensageriaService<DadosCarteiraResponse>> getDadosCarteira(@PathVariable UUID idColaborador) {
        return ok(
                new MensageriaService<DadosCarteiraResponse>()
                        .data(service.getDadosCarteira(idColaborador))
                        .status(200)
                        .mensagemCliente("Dados carteira")
        );
    }

    @PatchMapping("/atualizar-saldo/{id}")
    public ResponseEntity<Void> atualizarSaldo(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizacaoSaldo dto
    ) {
        service.atualizarSaldo(id, dto);
        return noContent().build();
    }

//    @PatchMapping("/sacar/{id}")
//    public ResponseEntity<Void> solicitarSaque(
//            @PathVariable UUID id,
//            @RequestBody @Valid AtualizacaoSaldo dto
//    ) {
//        service.solicitarSaque(id, dto);
//        return noContent().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

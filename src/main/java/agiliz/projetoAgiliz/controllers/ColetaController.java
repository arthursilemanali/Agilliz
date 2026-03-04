package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.coleta.*;
import agiliz.projetoAgiliz.services.ColetaService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/coletas")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class ColetaController {
    @Autowired
    private ColetaService service;

    @GetMapping("/campos")
    public ResponseEntity<Map<String, String>> getCamposColeta() {
        return ok(
                service.getCampos()
        );
    }

    @GetMapping("/disponiveis-conferencia")
    public ResponseEntity<List<ColetaResponse>> getDisponiveisConferencia(){
        return ok(
                service.getDispiniveisConferencia()
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ColetaResponse>> findByFilter(@RequestParam String campo, @RequestParam String valor){
        return ok(service.findByFilters(campo, valor));
    }

    @GetMapping("/finalizadas")
    public ResponseEntity<MensageriaService<List<ColetaPacotes>>> getColetasFinalizadas() {
        return ok(
                new MensageriaService<List<ColetaPacotes>>()
                        .mensagemCliente("Coletas concluidas")
                        .data(service.getAllConcluidas())
                        .status(200)
        );
    }

    @GetMapping("/{idColaborador}/finalizadas")
    public ResponseEntity<List<ColetaResponse>> getColetasFinalizadasByIdColaborador(@PathVariable String idColaborador) {
        return ok(service.getColetasFinalizadasByIdColaborador(idColaborador));
    }

    @PatchMapping("/{idColeta}/{codigo}")
    public ResponseEntity<Void> alterarStatusColeta(@PathVariable String idColeta,
                                                    @PathVariable Integer codigo) {
        service.alterarStatus(idColeta, codigo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/ml")
    public ResponseEntity<Void> cadastrarMl(@RequestBody ColetaMLRequest coleta) {
        service.cadastrarML(coleta);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MensageriaService<ColetaResponse>> cadastrar(
            @RequestBody @Valid ColetaPostRequest dto
    ) {
        return ok(
                new MensageriaService<ColetaResponse>()
                        .data(service.inserir(dto))
                        .mensagemCliente("Coleta cadastrada com sucesso")
                        .status(200)
        );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<ColetaResponse>>> getColetas() {
        var coletas = service.getColetasResponse();

        if (coletas.isEmpty()) return noContent().build();

        return ok(
                new MensageriaService<List<ColetaResponse>>()
                        .data(coletas)
                        .mensagemCliente("Coletas:")
                        .status(200)
        );
    }

    @GetMapping("/em-espera")
    public ResponseEntity<MensageriaService<List<ColetaResponse>>> getColetasAbertas() {
        return ok(
                new MensageriaService<List<ColetaResponse>>()
                        .data(service.getColetasAbertas())
                        .mensagemCliente("Coletas abertas:")
                        .status(200)
        );
    }

    @GetMapping("/pacotes")
    public ResponseEntity<MensageriaService<List<ColetaPacotes>>> getPacotesColeta(@RequestParam List<String> idColeta) {
        return ok(
                new MensageriaService<List<ColetaPacotes>>()
                        .data(service.getPacoteColetas(idColeta))
                        .mensagemCliente("Coletas abertas:")
                        .status(200)
        );
    }

    @PutMapping("/{idColeta}/{idColaborador}")
    public ResponseEntity<Void> associarColaborador(@PathVariable String idColeta,
                                                    @PathVariable String idColaborador,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        service.associarColaborador(idColeta, idColaborador, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/desassociar/{idColeta}/{idColaborador}")
    public ResponseEntity<Void> desassociarColaborador(@PathVariable String idColeta, @PathVariable String idColaborador) {
        service.desassociarColaborador(idColeta, idColaborador);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nao-atribuidas")
    public ResponseEntity<MensageriaService<List<ColetaResponse>>> getColetasNaoAtribuidas() {
        return ok(
                new MensageriaService<List<ColetaResponse>>()
                        .data(service.getColetasNaoAtribuidas())
                        .mensagemCliente("Coletas não atribuidas")
                        .status(200)
        );
    }

    @GetMapping("/colaborador/{idColaborador}")
    public ResponseEntity<MensageriaService<List<ColetaResponse>>> getColetasPorIdColaborador(@PathVariable String idColaborador) {
        return ok(
                new MensageriaService<List<ColetaResponse>>()
                        .data(service.getColetasAtribuidas(idColaborador))
                        .mensagemCliente("Coletas atribuídas")
                        .status(200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<ColetaResponse>> getColetaPorId(@PathVariable UUID id) {
        return ok(
                new MensageriaService<ColetaResponse>()
                        .data(service.getResponsePorId(id))
                        .mensagemCliente("Coleta")
                        .status(200)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<ColetaResponse>> alterar(
            @PathVariable UUID id,
            @RequestBody @Valid ColetaPutRequest dto
    ) {
        return ok(
                new MensageriaService<ColetaResponse>()
                        .data(service.alterar(id, dto))
                        .mensagemCliente("Coleta alterada com sucesso")
                        .status(200)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}

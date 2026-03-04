package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.colaborador.AssociarColaboradorPacote;
import agiliz.projetoAgiliz.dto.pacote.*;
import agiliz.projetoAgiliz.dto.roteirizacao.RegistroSaida;
import agiliz.projetoAgiliz.dto.roteirizacao.Roteirizacao;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.PacoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
@RequestMapping("/pacotes")
@CrossOrigin
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class PacoteController {
    private final PacoteService service;

    @PatchMapping("/entregar/{idPacote}")
    public ResponseEntity<Void> entregarPacote(@PathVariable String idPacote){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/associar-colaborador")
    public ResponseEntity<Void> associarPacoteColaborador(@RequestBody @Valid AssociarColaboradorPacote dto){
        service.associarColaboradorPacote(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/colaborador/{idColaborador}")
    public ResponseEntity<MensageriaService<List<PacoteResponse>>> getPacotesParaEntregarPorIdColaborador(
            @PathVariable String idColaborador){
        return status(HttpStatus.OK)
                .body(new MensageriaService<List<PacoteResponse>>()
                        .mensagemCliente("Pacotes: ")
                        .status(HttpStatus.OK.value())
                        .data(service.getPacotesParaEntregarPorIdColaborador(idColaborador)));
    }

    @PatchMapping("/ecommerce/{idColaborador}")
    public ResponseEntity<MensageriaService<List<PacoteResponse>>> getByIdEcommerce(@RequestBody @NotEmpty List<Integer> idsEcommerce
                                                                                    , @PathVariable String idColaborador) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MensageriaService<List<PacoteResponse>>()
                        .data(service.getByEcommerceId(idsEcommerce, idColaborador))
                        .status(200)
                        .mensagemCliente("Pacotes pelo ID do ecommerce:"));
    }

    @GetMapping("/pacotes-em-espera")
    public ResponseEntity<MensageriaService<List<ColetasResponseEmEspera>>> getPacotesEmEspera() {
        return status(HttpStatus.OK)
                .body(
                    new MensageriaService<List<ColetasResponseEmEspera>>()
                        .mensagemCliente("Coletas em espera")
                        .status(200)
                        .data(service.findAllPacotesEmEspera())
                );
    }

    @GetMapping("/pacotes-coletas")
    public ResponseEntity<MensageriaService<Page<PacoteResponse>>> getColetasPaginadas(
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "5") int tamanho
    ) {
        return ok(
            new MensageriaService<Page<PacoteResponse>>()
                .data(service.getPacotesColeta(pagina, tamanho))
                .mensagemCliente("Coletas:")
                .status(200)
        );
    }

    @PostMapping
    public ResponseEntity<MensageriaService<PacoteResponse>> cadastrar(
        @RequestBody @Valid PacoteRequest dto
    ) {
        return status(HttpStatus.CREATED)
                .body(
                    new MensageriaService<PacoteResponse>()
                        .mensagemCliente("Pacote inserido com sucesso")
                        .status(201)
                        .data(service.inserir(dto))
                );
    }

    @PostMapping("/roteirizacao")
    public ResponseEntity<MensageriaService<Roteirizacao>> registrarSaida(
            @RequestBody @Valid RegistroSaida dto
    ) {
        return ok(
                new MensageriaService<Roteirizacao>()
                    .data(service.roteirizarPacotes(dto))
                    .status(200)
                    .mensagemCliente("Entregas:")
        );
    }

    @PostMapping("/coleta")
    public ResponseEntity<MensageriaService<List<PacoteResponse>>> cadastrarEntrega(
        @RequestBody @Valid PacotesColetaRequest dto
    ) {
        return ok(
            new MensageriaService<List<PacoteResponse>>()
                .data(service.inserirColeta(dto))
                .mensagemCliente("Coletas cadastradas com sucesso")
                .status(200)
        );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<Page<PacoteResponse>>> listarPaginado(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho
    ) {
        var pacotes = service.getPacotesResponsePaginados(pagina, tamanho);

        if (pacotes.isEmpty()) return status(HttpStatus.NO_CONTENT).build();

        return status(HttpStatus.OK).body(
                new MensageriaService<Page<PacoteResponse>>()
                        .mensagemCliente("Pacotes:")
                        .data(pacotes)
                        .status(200)
        );
    }

    @GetMapping("/coletas")
    public ResponseEntity<MensageriaService<Page<ColetasResponse>>> getColetas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho
    ) {
        return noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<PacoteResponse>> getPacotePorId(@PathVariable UUID id) {
        return status(HttpStatus.OK)
                .body(
                        new MensageriaService<PacoteResponse>()
                                .mensagemCliente("Pacote:")
                                .data(service.getResponsePorId(id))
                                .status(200)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<PacoteResponse>> alterar(
            @PathVariable UUID id,
            @RequestBody @Valid PacoteRequest dto
    ) {
        return status(HttpStatus.OK)
                .body(
                        new MensageriaService<PacoteResponse>()
                                .mensagemCliente("Pacote atualizado com sucesso")
                                .data(new PacoteResponse(service.atualizar(dto, id)))
                                .status(200)
                );
    }

    @PatchMapping("/alterar-status/{id}")
    public ResponseEntity<Void> alterarStatusPacote(
            @PathVariable UUID id,
            @RequestBody @Valid AlterarStatusRequest dto
    ) {
        service.alterarStatus(id, dto.status(), null);
        return noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletarPorId(id);
        return status(HttpStatus.ACCEPTED).build();
    }
}

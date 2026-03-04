package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.vigenciaPagamentoAut.VigenciaPagamentoAutReq;
import agiliz.projetoAgiliz.dto.vigenciaPagamentoAut.VigenciaPagamentoAutRes;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.VigenciaPagamentoAutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/vigencias-pagamento-aut")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
public class VigenciaPagamentoAutController {
    private final VigenciaPagamentoAutService service;

    @PostMapping
    public ResponseEntity<MensageriaService<VigenciaPagamentoAutRes>> cadastrar(
            @RequestBody @Valid VigenciaPagamentoAutReq dto
    ) {

        return ok(
                new MensageriaService<VigenciaPagamentoAutRes>()
                        .mensagemCliente("Vigência de Pagamento Automático cadastrado com sucesso!")
                        .data(service.inserir(dto))
                        .status(200)
        );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<VigenciaPagamentoAutRes>> getVigenciaPagamentoAut() {
        Optional<VigenciaPagamentoAutRes> vigencia = service.getVigenciaResponse();

        if(vigencia.isEmpty()) {
            return noContent().build();
        }

        return ok(
                new MensageriaService<VigenciaPagamentoAutRes>()
                        .mensagemCliente("Vigência")
                        .data(vigencia.get())
                        .status(200)
        );
    }

    @PatchMapping
    public ResponseEntity<MensageriaService<Void>> atualizar(
            @RequestBody @Valid VigenciaPagamentoAutReq dto
    ) {
        service.alterarDiaSemana(dto);
        return noContent().build();
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deletar() {
        service.deletar();
        return noContent().build();
    }
}

package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.OS.OSRequest;
import agiliz.projetoAgiliz.dto.OS.OSResponse;
import agiliz.projetoAgiliz.dto.OS.PagamentoOsRequest;
import agiliz.projetoAgiliz.dto.OS.PagamentoOsResponse;
import agiliz.projetoAgiliz.models.OS;
import agiliz.projetoAgiliz.services.ColaboradorService;
import agiliz.projetoAgiliz.services.EmissaoPagamentoService;
import agiliz.projetoAgiliz.services.OSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/os")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
public class OSController {
    @Autowired
    private ColaboradorService colaboradorService;

    @Autowired
    private EmissaoPagamentoService emissaoPagamentoService;

    @Autowired
    private OSService osService;

    @PostMapping("/confirmar-pagamento")
    public ResponseEntity<Void> confirmarOs(@RequestBody List<String> idsOs){
        osService.confirmarPagamentosOs(idsOs);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<PagamentoOsResponse>> listarPagamentos() {
        return new ResponseEntity<>(colaboradorService.getColaboradores().stream().map(
                        colaborador -> new PagamentoOsResponse(colaborador))
                .toList(), HttpStatus.OK
        );
    }

    @GetMapping("/aguardando-pagamento")
    public ResponseEntity<List<OSResponse>> listarPagamentosAguardandoPagamento(){
        return new ResponseEntity<>(
                osService.listarOsAguardandoPagamento()
                .stream().map(os-> new OSResponse(os)).toList(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarOs(@RequestBody OSRequest osRequest) {
        try {
            osService.cadastrarOS(osRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/historico")
    public ResponseEntity<List<OSResponse>> getHistoricoOs(){
        try {
            return new ResponseEntity<>(
                    osService.getHistorico(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            throw e;
        }
    }
}

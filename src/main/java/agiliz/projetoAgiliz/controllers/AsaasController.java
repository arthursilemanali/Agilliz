package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.asaas.boleto.BoletoRequestDTO;
import agiliz.projetoAgiliz.dto.asaas.boleto.BoletoResponseDTO;
import agiliz.projetoAgiliz.dto.asaas.nf.NfRequestDTO;
import agiliz.projetoAgiliz.dto.asaas.pagamento.PagamentoResponseDTO;
import agiliz.projetoAgiliz.dto.asaas.webHook.WebhookDTO;
import agiliz.projetoAgiliz.services.AsaasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asaas")
public class AsaasController {
    @Autowired
    private AsaasService asaasService;

    @PostMapping("/pagamentos")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
    public ResponseEntity<Void> cadastrarBoleto(@RequestBody BoletoRequestDTO boletoRequest) {
        try {
            asaasService.cadastrarBoleto(boletoRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST
                    , e.getMessage()
                    , HttpStatus.BAD_REQUEST.value());
        }
    }

    @PutMapping("/pagamentos") // Webhook - sem restrição
    public ResponseEntity<PagamentoResponseDTO> atualizarPagamento(@RequestBody WebhookDTO dto) {
        return ResponseEntity.ok(asaasService.atualizarStatusPagamento(dto));
    }

    @PostMapping("/nf")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
    public ResponseEntity<Void> cadastrarNf(@RequestBody NfRequestDTO nfRequest) {
        asaasService.agendarNf(nfRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/boleto/{idBoleto}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
    public ResponseEntity<BoletoResponseDTO> getBoletoById(@PathVariable String idBoleto) {
        return ResponseEntity.ok(asaasService.getBoletoPorId(idBoleto));
    }

    @DeleteMapping("/boleto/{idBoleto}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_FINANCEIRO')")
    public ResponseEntity<Void> deletarBoletoPorId(@PathVariable String idBoleto) {
        asaasService.deletarBoletoPorId(idBoleto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

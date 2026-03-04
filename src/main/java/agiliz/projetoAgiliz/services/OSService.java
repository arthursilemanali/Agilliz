package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.dto.OS.OSRequest;
import agiliz.projetoAgiliz.dto.OS.OSResponse;
import agiliz.projetoAgiliz.dto.OS.PagamentoOsResponse;
import agiliz.projetoAgiliz.models.OS;
import agiliz.projetoAgiliz.repositories.IOSRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OSService {
    private final IOSRepository repository;
    private final PacoteService pacoteService;
    private final EmissaoPagamentoService emissaoPagamentoService;

    public List<OSResponse> getHistorico(){
        return repository.findHistorico().stream().map(OSResponse::new).toList();
    }

    public List<OS> findOSsById(List<String> ids){
        return repository.findAllById(ids.stream()
                .map(id-> UUID.fromString(id))
                .toList());
    }

    @Transactional
    public void confirmarPagamentosOs(List<String> ids){
       var osParaPagar = findOSsById(ids);
       for (var os : osParaPagar){
           os.setStatus(1);
       }
       repository.saveAll(osParaPagar);
    }

    public void cadastrarOS(OSRequest osRequest) {
        var pagamentos = emissaoPagamentoService.listAllByIds(
                osRequest.emissoes().stream()
                        .map(emissao -> UUID.fromString(emissao.fkPagamento()))
                        .toList()
        );

        for (var pagamento : pagamentos){
            pagamento.setStatusPagamento(3);
        }
        OS os = new OS();
        // if else 🤢
        // KKKKKKKKKKKKKKKKKKKKKKK CALMA ELSO 🥶
        if (pagamentos.size() > 1) {
            os.setTipo(2);
        } else {
            os.setTipo(1);
        }

        os.setValor(osRequest.valor());
        os.setStatus(2);
        repository.save(os);
        emissaoPagamentoService.atualizarEmissaoPagamento(pagamentos);
    }

    public List<OS> listarOsAguardandoPagamento(){
        return repository.findAllOSAguardandoPagamento();
    }
}

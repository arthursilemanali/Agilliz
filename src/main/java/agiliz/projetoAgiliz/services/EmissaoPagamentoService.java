package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.dto.OS.PagamentoOsRequest;
import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoDespesaResponse;
import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoResponse;
import agiliz.projetoAgiliz.dto.setor.BonificacaoLider;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.EmissaoPagamento;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.Pagamento;
import agiliz.projetoAgiliz.repositories.IEmissaoPagamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmissaoPagamentoService {
    private static final Logger logger = LoggerFactory.getLogger(EmissaoPagamentoService.class);
    private final IEmissaoPagamentoRepository repository;
    private final ColaboradorService colaboradorService;
    private final CarteiraService carteiraService;

    @Transactional
    public void cadastrar(PagamentoOsRequest pagamentoOsRequest) {
        logger.info("[EmissaoPagamentoService.cadastrar] Cadastrando emissao de pagamento");
        var colaborador = colaboradorService.getPorId(UUID.fromString(pagamentoOsRequest.idColaborador()));
        if(colaborador.getCarteira()==null) throw new IllegalArgumentException("Carteira inexistente");
        if(pagamentoOsRequest.valor()>colaborador.getCarteira().getSaldo()) throw new IllegalArgumentException("Valor a sacar não pode ser maior que o saldo");

        repository.save(new EmissaoPagamento(colaborador, pagamentoOsRequest.valor()));

        carteiraService.sacar(pagamentoOsRequest.valor(),
                UUID.fromString(pagamentoOsRequest.idColaborador()));
        logger.info("[EmissaoPagamentoService.cadastrar] Sucesso");
    }

    @Transactional
    public void atualizarEmissaoPagamento(List<EmissaoPagamento> pagamentos) {
        logger.info("[EmissaoPagamentoService.cadastrar] Cadastrando emissao de pagamento");
        repository.saveAll(pagamentos);
        logger.info("[EmissaoPagamentoService.cadastrar] Sucesso");
    }

    public List<EmissaoPagamentoDespesaResponse> obterCustosOperacionaisPorMes(LocalDateTime start, LocalDateTime end) {
        List<Object[]> resultados = repository.findCustoOperacionalPorMes(start.toLocalDate(), end.toLocalDate());
        List<EmissaoPagamentoDespesaResponse> custosPorMes = new ArrayList<>();

        for (Object[] resultado : resultados) {
            int mes = (Integer) resultado[0];
            double custoTotal = (Double) resultado[1];

            String nomeMes = obterNomeMes(mes);
            custosPorMes.add(new EmissaoPagamentoDespesaResponse(nomeMes, custoTotal));
        }

        return custosPorMes;
    }

    private String obterNomeMes(int mes) {
        switch (mes) {
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
        }
        return "";
    }

    private Double calcularValorOs(Colaborador colaborador, List<Pacote> pacotesParaPagar) {
        Double total = 0.0;
        for (var pacote : pacotesParaPagar) {
            for (var pagamento : colaborador.getPagamentos()) {
                if (pagamento.getTipoPagamento().getCodigo() != 3 &&
                        pagamento.getTipoPagamento().getOrigemRemunerada() == pacote.getOrigem()) {
                    total += pagamento.getRemuneracao();
                }
            }
        }
        return total;
    }

    public void cadastrar(Colaborador colaborador, Double valor) {
        repository.save(new EmissaoPagamento(colaborador, valor));
    }

    public void cadastrar(List<Pagamento> pagamentos) {
        var emissoes = pagamentos.stream()
                .map(EmissaoPagamento::new)
                .toList();

        repository.saveAll(emissoes);
    }

    public void cadastrarBonificacao(List<BonificacaoLider> bonificacoes) {
        List<EmissaoPagamento> emissoes = bonificacoes.stream()
                .map(EmissaoPagamento::new)
                .toList();

        repository.saveAll(emissoes);
    }

    public List<EmissaoPagamentoResponse> getEmissoesResponse() {
        return repository.findAllResponse();
    }

    public Double getCustoOperacional(LocalDateTime start, LocalDateTime end) {
        logger.info("[EmissaoPagamentoService.gerarFolhaDePagamento] Consultando o valor do custo operacional");

        Double custoOperacional = repository.getCustoOperacional(start.toLocalDate(), end.toLocalDate());

        logger.info("[EmissaoPagamentoService.gerarFolhaDePagamento] Custo operacional: {}", custoOperacional);
        return custoOperacional;
    }

    public List<EmissaoPagamento> listAllByIds(List<UUID> ids) {
        return repository.findAllById(ids);
    }
}

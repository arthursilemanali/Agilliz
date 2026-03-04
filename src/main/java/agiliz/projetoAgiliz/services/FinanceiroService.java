package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.dto.dashFinanceira.DadosFinanceiros;
import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoDespesaResponse;
import agiliz.projetoAgiliz.repositories.IPacoteRepository;
import agiliz.projetoAgiliz.repositories.IPrecificacaoZona;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceiroService {
    private final EmissaoPagamentoService emissaoPagamentoService;
    private final IPacoteRepository pacoteRepository;
    private final VendedorService vendedorService;
    private final EmissaoDespesaService emissaoDespesaService;
    private final IPrecificacaoZona precificacaoZona;
    private final ZonaService zonaService;
    private final DespesaService despesaService;
    private final AsaasService asaasService;

    private static final Logger logger = LoggerFactory.getLogger(FinanceiroService.class);

    public DadosFinanceiros getDadosDash(LocalDate startDate, LocalDate endDate) {
        logger.info("[FinanceiroService.getDadosDash] Início da geração dos dados financeiros para o dashboard");
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        var tickets = precificacaoZona.findZonaPrices();
        var slaBoletos = asaasService.getSLABoletos(startDate, endDate);
        var boletosAndamento = asaasService.getBoletosEmAndamento(startDate, endDate);

        Double lucroBruto = pacoteRepository.calcularValorTotalPacotes(startDateTime, endDateTime);
        lucroBruto = (lucroBruto != null) ? lucroBruto : 0.0;

        Double ticketMedio;
        Double somaTickets;

        if(tickets.isEmpty()) {
            ticketMedio = 0.;
        } else {
            somaTickets = tickets.stream()
                    .mapToDouble(v -> v)
                    .sum();

            ticketMedio = somaTickets / tickets.size();
        }
        var despesasVariaveis = despesaService.getTotalDespesasVariaveis(startDate, endDate);
        var despesasFixas = despesaService.getTotalDespesasFixas(startDate, endDate);
        var totalImpostos = despesaService.getTotalImposto(startDate, endDate);
        var custoOperacional = emissaoPagamentoService.obterCustosOperacionaisPorMes(startDateTime, endDateTime);
        var custoOperacionalTotal = emissaoPagamentoService.getCustoOperacional(startDateTime, endDateTime);
        var totalDespesas = (despesasVariaveis != null ? despesasVariaveis : 0) +
                (despesasFixas != null ? despesasFixas : 0) +
                (custoOperacionalTotal != null ? custoOperacionalTotal : 0) +
                (totalImpostos != null ? totalImpostos : 0);
        var lucroLiquido = lucroBruto - totalDespesas;

        DadosFinanceiros dadosFinanceiros = new DadosFinanceiros(
                lucroBruto,
                lucroLiquido,
                ticketMedio,
                despesasFixas,
                despesasVariaveis,
                totalImpostos,
                0.,
                custoOperacional,
                slaBoletos,
                boletosAndamento
        );

        logger.info("[FinanceiroService.getDadosDash] Dados financeiros gerados com sucesso");

        return dadosFinanceiros;
    }
}
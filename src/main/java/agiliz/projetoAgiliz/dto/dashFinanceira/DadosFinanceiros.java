package agiliz.projetoAgiliz.dto.dashFinanceira;

import agiliz.projetoAgiliz.dto.asaas.boleto.BoletoDashDTO;
import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoDespesaResponse;
import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoResponse;

import java.util.List;

public record DadosFinanceiros(
    Double lucroBruto,
    Double lucroLiquido,
    Double ticketMedio,
    Double totalDespesasFixas,
    Double totalDespesasVariaveis,
    Double totalImposto,
    Double taxas,
    List<EmissaoPagamentoDespesaResponse> custoOperacional,
    List<BoletoDashDTO> boletosSLA,
    List<BoletoDashDTO> boletosEmAndamento
) {}

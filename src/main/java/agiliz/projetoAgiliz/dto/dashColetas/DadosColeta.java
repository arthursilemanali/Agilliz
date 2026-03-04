package agiliz.projetoAgiliz.dto.dashColetas;

import agiliz.projetoAgiliz.dto.coleta.ColetaResponse;
import agiliz.projetoAgiliz.dto.pacote.ColetasResponse;

import java.util.List;

public record DadosColeta(
        long pacotesColetados,
        long pacotesAusentes,
        long pacotesEmRota,
        long pacotesAguardandoColeta,
        long coletasRealizadas,
        long coletasCanceladas,
        List<ColetasPorTempo> coletasPorTempo,
        Integer totalDevolvidas,
        List<ColetasEmAndamento> coletas,
        List<ColetasSLA> slaColetas,
        Double mediaHorarioCorte,
        long quantidadeColetas,
        long coletasEmAberto,
        Double taxaPerformance,
        List<ColetaResponse> allColetas
) {
}

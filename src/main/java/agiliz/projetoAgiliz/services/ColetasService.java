package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.dto.coleta.ColetaResponse;
import agiliz.projetoAgiliz.dto.dashColetas.DadosColeta;
import agiliz.projetoAgiliz.enums.StatusColeta;
import agiliz.projetoAgiliz.enums.StatusPacote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ColetasService {

    private final PacoteService pacoteService;
    private final ColetaService coletaService;

    public DadosColeta getDadosColeta(LocalDateTime start, LocalDateTime end) {
        var pacotesStatusOnly = pacoteService.getAllPacoteStatusOnly(start, end);
        var dadosColeta = coletaService.getColetasEmEspera();
        var slaColetas = coletaService.getSlaColetas(start, end);
        var countColetasAbertas = coletaService.getColetasAbertas().size();
        var countColetas = coletaService.countColetas();
        var coletas=coletaService.listarTodas();
        var taxaPerformance = coletas.isEmpty() ? 0 :
                (coletas.stream()
                        .filter(c -> c.getStatusColeta() == StatusColeta.CONCLUIDA || c.getStatusColeta() == StatusColeta.PRONTA_PARA_CONFERENCIA)
                        .count() / (double) coletas.size()) * 100;
        var coletasAll = coletas.stream().map(ColetaResponse::new).toList();

        return new DadosColeta(
                pacotesStatusOnly.stream().filter(pacote -> pacote.getStatus() == StatusPacote.CONCLUIDO).toList().size(),
                pacotesStatusOnly.stream().filter(pacote -> pacote.getStatus() == StatusPacote.AUSENTE).toList().size(),
                pacotesStatusOnly.stream().filter(pacote -> pacote.getStatus() == StatusPacote.A_CAMINHO).toList().size(),
                pacotesStatusOnly.stream().filter(pacote -> pacote.getStatus() == StatusPacote.EM_ESPERA).toList().size(),
                pacoteService.getQuantidadeColetasRealizadas(start, end),
                pacoteService.getQuantidadeColetasCanceladas(start, end),
                pacoteService.getColetasPorTempo(start, end),
                pacoteService.countDevolvidas(start, end),
                dadosColeta,
                slaColetas,
                pacotesStatusOnly.stream()
                        .filter(p -> p.getVendedor() != null && p.getVendedor().getHorarioCorte() != null)
                        .mapToInt(p -> p.getVendedor().getHorarioCorte().toSecondOfDay() / 60)
                        .average()
                        .orElse(0),
                countColetas,
                countColetasAbertas,
                taxaPerformance,
                coletasAll

        );
    }
}

package agiliz.projetoAgiliz.dto.OS;

import agiliz.projetoAgiliz.dto.zona.ZonaResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.OS;

import java.util.List;

public record PagamentoOsResponse(
        String nomeColaborador,
        String cpfColaborador,
        Integer pacotesEntregues,
        List<ZonaResponse> zonas,
        Double valor,
        String chavePix
) {
    public PagamentoOsResponse(Colaborador colaborador) {
        this(
                colaborador.getNomeColaborador(),
                colaborador.getCpf(),
                calcularPacotesEntregues(colaborador),
                calcularZonas(colaborador),
                colaborador.getCarteira().getSaldo(),
                colaborador.getCarteira().getChavePix()
        );
    }

    private static Integer calcularPacotesEntregues(Colaborador colaborador) {
        return (int) colaborador.getPacotes().stream()
                .filter(pacote -> pacote.getStatus().getCodigo() == 2)
                .count();
    }

    private static List<ZonaResponse> calcularZonas(Colaborador colaborador) {
        return colaborador.getPacotes().stream()
                .filter(pacote -> pacote.getStatus().getCodigo() == 2)
                .map(pacote -> new ZonaResponse(pacote.getZona()))
                .toList();
    }
}

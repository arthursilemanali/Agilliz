package agiliz.projetoAgiliz.dto.colaborador;

import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.PrecificacaoZona;

import java.util.List;

public record ColaboradorPacoteResponse(
        String nomeColaborador,
        List<PacoteResponse> pacotes,
        Double totalRetorno
) {
    public ColaboradorPacoteResponse(Colaborador colaborador, List<Pacote> pacotes) {
        this(
                colaborador.getNomeColaborador(),
                pacotes.stream().map(PacoteResponse::new).toList(),
                calcularTotalRetorno(pacotes)
        );
    }

    private static Double calcularTotalRetorno(List<Pacote> pacotes) {
        return pacotes.stream().mapToDouble(pacote ->
                pacote.getVendedor().getZonas().stream()
                        .filter(zona -> zona.getTipoZona().equals(pacote.getZona().getTipoZona()))
                        .mapToDouble(PrecificacaoZona::getPreco)
                        .sum()
        ).sum();
    }
}

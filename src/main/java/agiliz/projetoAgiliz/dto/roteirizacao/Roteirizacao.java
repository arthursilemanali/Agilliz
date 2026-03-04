package agiliz.projetoAgiliz.dto.roteirizacao;

import agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao.MetricasTrajeto;
import agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao.RoteirizacaoResponse;
import agiliz.projetoAgiliz.models.Pacote;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record Roteirizacao(
    Coordenada inicio,
    List<Entrega> entregas,
    Coordenada fim,
    MetricasTrajeto metricas
) {
    public Roteirizacao(
        RoteirizacaoResponse response,
        Map<UUID, Pacote> pacotes
    ) {
        this(
            response.inicio(),
            response.paradas().stream().map(p -> new Entrega(pacotes.get(p.idPacote()))).toList(),
            response.fim(),
            response.metricas()
        );
    }
}

package agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao;

import agiliz.projetoAgiliz.dto.roteirizacao.Coordenada;
import agiliz.projetoAgiliz.dto.roteirizacao.Parada;

import java.util.List;

public record RoteirizacaoResponse(
    Coordenada inicio,
    List<Parada> paradas,
    Coordenada fim,
    MetricasTrajeto metricas
) {}


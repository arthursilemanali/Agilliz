package agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao;

public record MetricasTrajeto(
    Double distanciaTotal,
    TempoEstimado tempoEstimado,
    String horaTerminoEstimada,
    Integer totalParadas
) {}

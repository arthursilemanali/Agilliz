package agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao;

import agiliz.projetoAgiliz.dto.roteirizacao.Coordenada;
import agiliz.projetoAgiliz.dto.roteirizacao.Parada;

import java.util.List;

public record RoteirizacaoRequest(
    List<Parada> paradas,
    Coordenada inicio,
    Coordenada fim
) {}


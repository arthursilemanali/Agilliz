package agiliz.projetoAgiliz.dto.setor;

import agiliz.projetoAgiliz.models.Colaborador;


public record BonificacaoLider(
        Colaborador lider,
        Long qtdPacotes,
        Double valorBonificacao
) {
    public Double totalBonificacao() {
        return qtdPacotes * valorBonificacao;
    }
}

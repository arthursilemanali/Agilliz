package agiliz.projetoAgiliz.dto.vigenciaPagamentoAut;


import agiliz.projetoAgiliz.models.VigenciaPagamentoAut;

import java.time.LocalDateTime;
import java.util.UUID;

public record VigenciaPagamentoAutRes(
        UUID idVigenciaPagamentoAut,
        LocalDateTime dataHoraCriacao,
        LocalDateTime dataHoraAtualizacao,
        String diaSemana
) {

    public VigenciaPagamentoAutRes(VigenciaPagamentoAut vigencia) {
        this(
                vigencia.getIdVigenciaPagamentoAut(),
                vigencia.getDataHoraCriacao(),
                vigencia.getDataHoraAtualizacao(),
                vigencia.getDiaSemanaAlias()
        );
    }

}

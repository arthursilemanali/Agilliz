package agiliz.projetoAgiliz.dto.transacao;

import agiliz.projetoAgiliz.models.Transacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponse(
        UUID idTransacao,
        Double valor,
        String automatica,
        LocalDateTime dataHoraTransacao,
        String colaborador
) {

    public TransacaoResponse(Transacao transacao) {
        this(
                transacao.getIdTransacao(),
                transacao.getValor(),
                transacao.isAutomatica() ? "Sim" : "Não",
                transacao.getDataHoraTransacao(),
                transacao.getColaborador().getNomeColaborador()
        );
    }

}

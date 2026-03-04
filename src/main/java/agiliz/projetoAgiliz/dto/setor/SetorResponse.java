package agiliz.projetoAgiliz.dto.setor;


import agiliz.projetoAgiliz.dto.colaborador.ColaboradorResponse;
import agiliz.projetoAgiliz.models.Setor;

import java.util.UUID;

public record SetorResponse(
        UUID idSetor,
        String limiteInferiorCep,
        String limiteSuperiorCep,
        ColaboradorResponse liderSetor,
        Double valorBonificacao,
        Integer quantidadeMembros
) {

    public SetorResponse(Setor setor) {
        this(
                setor.getIdSetor(),
                setor.getLimiteInferiorCep(),
                setor.getLimiteSuperiorCep(),
                setor.getLiderSetor() != null ? new ColaboradorResponse(setor.getLiderSetor()) : null,
                setor.getValorBonificacao(),
                setor.getMembros() != null ? setor.getMembros().size() : 0
        );
    }

}

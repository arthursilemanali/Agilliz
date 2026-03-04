package agiliz.projetoAgiliz.dto.membroSetor;

import agiliz.projetoAgiliz.dto.colaborador.ColaboradorResponse;
import agiliz.projetoAgiliz.dto.setor.SetorResponse;
import agiliz.projetoAgiliz.models.MembroSetor;

import java.time.LocalDate;
import java.util.UUID;

public record MembroSetorResponse(
        UUID idMembroSetor,
        ColaboradorResponse colaborador,
        SetorResponse setor,
        LocalDate dataAlocacao
) {
    public MembroSetorResponse(MembroSetor membro) {
        this(
                membro.getIdMembroSetor(),
                new ColaboradorResponse(membro.getColaborador()),
                new SetorResponse(membro.getSetor()),
                membro.getDataAlocacao()
        );
    }
}

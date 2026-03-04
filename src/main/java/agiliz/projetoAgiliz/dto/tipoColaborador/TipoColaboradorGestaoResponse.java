package agiliz.projetoAgiliz.dto.tipoColaborador;

import agiliz.projetoAgiliz.models.TipoColaborador;

public record TipoColaboradorGestaoResponse(
        boolean taxado,
        String descricao
) {
    public TipoColaboradorGestaoResponse(TipoColaborador tipo) {
        this(
                tipo.isTaxado(),
                tipo.getDescricao()
        );
    }
}

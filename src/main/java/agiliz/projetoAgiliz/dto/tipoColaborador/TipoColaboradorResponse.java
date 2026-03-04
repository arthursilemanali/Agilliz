package agiliz.projetoAgiliz.dto.tipoColaborador;

import agiliz.projetoAgiliz.models.TipoColaborador;

import java.util.UUID;

public record TipoColaboradorResponse(
    UUID idTipoColaborador,
    String taxado,
    String descricao
) {

    public TipoColaboradorResponse(TipoColaborador tipo) {
        this(
            tipo.getIdTipoColaborador(),
            tipo.isTaxado() ? "Sim" : "Não",
            tipo.getDescricao()
        );
    }
}

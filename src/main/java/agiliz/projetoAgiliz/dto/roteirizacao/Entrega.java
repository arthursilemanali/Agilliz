package agiliz.projetoAgiliz.dto.roteirizacao;

import agiliz.projetoAgiliz.dto.destinatario.DestinatarioResponse;
import agiliz.projetoAgiliz.models.Pacote;

import java.util.UUID;

public record Entrega(
    UUID idPacote,
    Coordenada coordenada,
    String origem,
    Integer quantidadeTentativasEntrega,
    String observacao,
    DestinatarioResponse destinatario
) {
    public Entrega(Pacote pacote) {
        this(
            pacote.getIdPacote(),
            pacote.getCoordenadas(),
            pacote.getOrigem().getAlias(),
            pacote.getQuantidadeTentativasEntrega(),
            pacote.getObservacao(),
            new DestinatarioResponse(pacote.getDestinatario())
        );
    }
}

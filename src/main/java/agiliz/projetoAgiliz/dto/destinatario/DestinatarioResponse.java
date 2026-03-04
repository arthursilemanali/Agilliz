package agiliz.projetoAgiliz.dto.destinatario;

import agiliz.projetoAgiliz.models.Destinatario;

import java.util.UUID;

public record DestinatarioResponse(
        UUID idDestinatario,
        String nome,
        String rua,
        String cep,
        Integer numero,
        String bairro,
        String estado
) {
    public DestinatarioResponse(Destinatario destinatario) {
        this(
                destinatario != null && destinatario.getIdDestinatario() != null ? destinatario.getIdDestinatario() : null,
                destinatario != null ? destinatario.getNome() : null,
                destinatario != null ? destinatario.getRua() : null,
                destinatario != null ? destinatario.getCep() : null,
                destinatario != null ? destinatario.getNumero() : null,
                destinatario != null ? destinatario.getBairro() : null,
                destinatario != null ? destinatario.getEstado() : null
        );
    }
}

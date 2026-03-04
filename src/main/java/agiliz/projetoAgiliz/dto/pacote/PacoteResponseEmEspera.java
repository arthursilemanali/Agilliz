package agiliz.projetoAgiliz.dto.pacote;

import agiliz.projetoAgiliz.models.Pacote;

import java.util.List;

public record PacoteResponseEmEspera(
        String idPacote,
        String cep,
        String endereco,
        String tipo
) {
    public PacoteResponseEmEspera(Pacote pacote) {
        this(
                pacote.getIdPacote().toString(),
                pacote.getDestinatario() == null ? " " : pacote.getDestinatario().getCep(),
                pacote.getDestinatario() == null ? " " : formatEndereco(pacote),
                pacote.getOrigem().getAlias()
        );
    }

    private static String formatEndereco(Pacote pacote) {
        var destinatario = pacote.getDestinatario();
        if (destinatario == null) {
            return " ";
        }
        StringBuilder endereco = new StringBuilder();
        endereco.append(destinatario.getRua() != null ? destinatario.getRua() : "")
                .append(", ")
                .append(destinatario.getNumero() != null ? destinatario.getNumero() : "");
        return endereco.toString();
    }
}

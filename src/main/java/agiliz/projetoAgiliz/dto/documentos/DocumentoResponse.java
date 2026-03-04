package agiliz.projetoAgiliz.dto.documentos;

import agiliz.projetoAgiliz.models.Documentos;

public record DocumentoResponse(
        String idColaboradorCTPS,
        String idColaboradorHabilitacao,
        String idColaboradorReservista,
        String idColaboradorResidencia
) {
    public DocumentoResponse(Documentos documentos) {
        this(
                documentos.getIdColaboradorCTPS() != null ? documentos.getIdColaboradorCTPS().toString() : null,
                documentos.getIdColaboradorHabilitacao() != null ? documentos.getIdColaboradorHabilitacao().toString() : null,
                documentos.getIdColaboradorReservista() != null ? documentos.getIdColaboradorReservista().toString() : null,
                documentos.getIdColaboradorResidencia() != null ? documentos.getIdColaboradorResidencia().toString() : null
        );
    }
}

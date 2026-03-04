package agiliz.projetoAgiliz.dto.registroManifesto;

import agiliz.projetoAgiliz.dto.manifesto.ManifestoResponse;
import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.models.RegistroManifesto;

import java.time.LocalDate;
import java.util.UUID;

public record RegistroManifestoResponse(
    UUID idRegistroManifesto,
    LocalDate dataResgistro,
    ManifestoResponse manifesto,
    PacoteResponse pacote
) {
    public RegistroManifestoResponse(RegistroManifesto registro) {
        this(
            registro.getIdRegistroManifesto(),
            registro.getDataResgistro(),
            new ManifestoResponse(registro.getManifesto()),
            new PacoteResponse(registro.getPacote())
        );
    }
}

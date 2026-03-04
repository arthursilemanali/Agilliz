package agiliz.projetoAgiliz.dto.manifesto;

import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.models.Manifesto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ManifestoResponse(
        UUID idManifesto,
        LocalDate dataEmitido,
        String tipo,
        String status,
        List<PacoteResponse> pacotes
) {
    public ManifestoResponse(Manifesto manifesto, List<PacoteResponse> pacotes) {
        this(
                manifesto.getIdManifesto(),
                manifesto.getDataEmitido(),
                manifesto.getTipo().getAlias(),
                manifesto.getStatus().getAlias(),
                pacotes
        );
    }
    public ManifestoResponse(Manifesto manifesto) {
        this(
                manifesto.getIdManifesto(),
                manifesto.getDataEmitido(),
                manifesto.getTipo().getAlias(),
                manifesto.getStatus().getAlias(),
                new ArrayList<>()
        );
    }
}

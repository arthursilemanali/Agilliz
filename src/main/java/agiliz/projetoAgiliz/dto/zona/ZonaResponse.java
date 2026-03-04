package agiliz.projetoAgiliz.dto.zona;

import agiliz.projetoAgiliz.models.Zona;

import java.util.UUID;

public record ZonaResponse(
    UUID idZona,
    String nomeZona,
    Integer limiteSuperiorCEP,
    Integer limiteInferiorCEP,
    String tipoZona,
    Integer pacotes
) {
    public ZonaResponse(Zona zona) {
        this(
                zona.getIdZona(),
                zona.getNomeZona(),
                zona.getLimiteSuperiorCEP(),
                zona.getLimiteInferiorCEP(),
                zona.getTipoZona().getAlias(),
                zona.getPacotes() == null ? 0
                        : zona.getPacotes().size()
        );
    }
}

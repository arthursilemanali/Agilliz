package agiliz.projetoAgiliz.dto.zona;

import agiliz.projetoAgiliz.models.Zona;

import java.util.UUID;

public record ZonaResponsePut(
        UUID idZona,
        String nomeZona,
        Integer limiteSuperiorCEP,
        Integer limiteInferiorCEP,
        Integer tipoZona,
        Integer pacotes,
        Double precoMedioZona
) {
    public ZonaResponsePut(Zona zona) {
        this(
                zona.getIdZona(),
                zona.getNomeZona(),
                zona.getLimiteSuperiorCEP(),
                zona.getLimiteInferiorCEP(),
                zona.getTipoZona().getCodigo(),
                zona.getPacotes().size(),
                zona.getPacotes().isEmpty() ? 0.0 : zona.getPacotes().stream()
                        .mapToDouble(pacote -> pacote.getVendedor() == null ? null : pacote.getVendedor().getRetornoTotal())
                        .sum()
        );
    }
}

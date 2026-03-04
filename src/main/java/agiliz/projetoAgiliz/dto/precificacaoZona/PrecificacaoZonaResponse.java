package agiliz.projetoAgiliz.dto.precificacaoZona;

import agiliz.projetoAgiliz.dto.unidade.VendedorResponse;
import agiliz.projetoAgiliz.models.PrecificacaoZona;

import java.util.UUID;

public record PrecificacaoZonaResponse(
    UUID idPrecificacaoZona,
    Double preco,
    String tipoZona,
    VendedorResponse unidade
) {
    public PrecificacaoZonaResponse(PrecificacaoZona precificacaoZona) {
        this(
            precificacaoZona.getIdPrecificacaoZona(),
            precificacaoZona.getPreco(),
            precificacaoZona.getTipoZona().getAlias(),
            new VendedorResponse(precificacaoZona.getVendedor())
        );
    }
}

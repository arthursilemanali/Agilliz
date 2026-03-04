package agiliz.projetoAgiliz.dto.precificacaoZona;

import agiliz.projetoAgiliz.enums.TipoZona;
import agiliz.projetoAgiliz.models.PrecificacaoZona;

import java.util.UUID;

public record PrecificacaoResponseNoVendedor(
        UUID idPrecificacaoZona,
        Double preco,
        Integer tipoZona,
        String zona
) {
    public PrecificacaoResponseNoVendedor(PrecificacaoZona precificacaoZona) {
        this(
                precificacaoZona.getIdPrecificacaoZona(),
                precificacaoZona.getPreco(),
                precificacaoZona.getTipoZona().getCodigo(),
                TipoZona.valueOf(precificacaoZona.getTipoZona().getCodigo()).getAlias()
        );
    }
}

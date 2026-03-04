package agiliz.projetoAgiliz.dto.coleta;

import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.dto.vendedor.VendedorColeta;
import agiliz.projetoAgiliz.models.Coleta;

import java.util.List;
import java.util.UUID;

public record ColetaPacotes(
        UUID idColeta,
        Integer romaneio,
        String statusColeta,
        VendedorColeta vendedor,
        List<PacoteResponse> pacotes
) {
    public ColetaPacotes(Coleta coleta) {
        this(
                coleta.getIdColeta(),
                coleta.getRomaneio(),
                coleta.getStatusColeta().getAlias(),
                coleta.getVendedor() == null ? null : new VendedorColeta(coleta.getVendedor()),
                coleta.getPacotes().stream()
                        .filter(p -> p.getColaborador() == null && p.getTipo().getCodigo() == 2)
                        .map(PacoteResponse::new)
                        .toList()
        );
    }
}

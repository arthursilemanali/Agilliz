package agiliz.projetoAgiliz.dto.dashColetas;

import agiliz.projetoAgiliz.models.Coleta;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public record ColetasEmAndamento(
        String cliente,
        List<String> coletor,
        Integer romaneio,
        Integer status
) {
    public ColetasEmAndamento(Coleta coleta) {
        this(
                coleta.getVendedor() == null ? "" : coleta.getVendedor().getNomeVendedor(),
                CollectionUtils.isEmpty(coleta.getColetores()) ? new ArrayList<>():
                        coleta.getColetores().stream().map(
                                c -> c.getColaborador() == null ? "" : c.getColaborador().getNomeColaborador()
                        ).toList(),
                coleta.getRomaneio(),
                coleta.getStatusColeta().getCodigo()
        );
    }
}

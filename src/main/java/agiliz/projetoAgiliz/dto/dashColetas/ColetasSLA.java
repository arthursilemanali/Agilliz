package agiliz.projetoAgiliz.dto.dashColetas;

import agiliz.projetoAgiliz.models.Coleta;
import org.springframework.util.CollectionUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record ColetasSLA(
        String cliente,
        List<String> coletor,
        Integer statusCodigo,
        String status,
        String data
) {
    public ColetasSLA(Coleta coleta) {
        this(
                coleta.getVendedor() == null ? "" : coleta.getVendedor().getNomeVendedor(),
                CollectionUtils.isEmpty(coleta.getColetores()) ? new ArrayList<>() :
                        coleta.getColetores().stream().map(
                                c -> c.getColaborador() == null ? "" : c.getColaborador().getNomeColaborador()
                        ).toList(),
                coleta.getStatusColeta().getCodigo(),
                coleta.getStatusColeta().getAlias(),
                coleta.getDiaHoraRegistro().format(DateTimeFormatter.ofPattern("dd MM yyyy"))
        );
    }
}
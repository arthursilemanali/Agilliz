package agiliz.projetoAgiliz.dto.OS;

import agiliz.projetoAgiliz.enums.StatusPagamentoEnum;
import agiliz.projetoAgiliz.models.OS;

import java.time.LocalDate;

public record OSResponse(
        String idOs,
        String tipoOs,
        Double valorTotal,
        LocalDate data,
        String status
) {
    public OSResponse(OS os){
        this(
          os.getIdOS().toString(),
          os.getTipo().getAlias(),
          os.getValor(),
          os.getDataEmissao(),
          os.getStatus().getAlias()
        );
    }
}

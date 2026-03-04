package agiliz.projetoAgiliz.dto.coleta;

import agiliz.projetoAgiliz.dto.pacote.PacoteMLRequest;

import java.util.List;

public record ColetaMLRequest(
        Integer romaneio,
        List<PacoteMLRequest> pacotes,
        String fkVendedor
) {
}

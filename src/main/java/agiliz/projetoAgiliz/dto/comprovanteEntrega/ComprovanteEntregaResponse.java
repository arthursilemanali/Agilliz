package agiliz.projetoAgiliz.dto.comprovanteEntrega;

import agiliz.projetoAgiliz.dto.destinatario.DestinatarioResponse;
import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.dto.unidade.VendedorResponse;
import agiliz.projetoAgiliz.dto.zona.ZonaResponse;
import agiliz.projetoAgiliz.models.ComprovanteEntrega;

import java.time.LocalDateTime;
import java.util.UUID;


public record ComprovanteEntregaResponse(
    UUID idComprovanteEntrega,
    VendedorResponse unidade,
    DestinatarioResponse destinatario,
    PacoteResponse pacote,
    ZonaResponse zona,
    LocalDateTime dataHoraEmissao
) {

    public ComprovanteEntregaResponse(ComprovanteEntrega comprovante) {
        this(
            comprovante.getIdComprovante(),
            new VendedorResponse(comprovante.getPacote().getVendedor()),
            new DestinatarioResponse(comprovante.getPacote().getDestinatario()),
            new PacoteResponse(comprovante.getPacote()),
            new ZonaResponse(comprovante.getPacote().getZona()),
            comprovante.getDataHoraEmissao()
        );
    }
}

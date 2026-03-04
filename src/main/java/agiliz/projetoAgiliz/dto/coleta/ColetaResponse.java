package agiliz.projetoAgiliz.dto.coleta;

import agiliz.projetoAgiliz.dto.coletor.ColetorResponse;
import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.dto.vendedor.VendedorColeta;
import agiliz.projetoAgiliz.enums.StatusPacote;
import agiliz.projetoAgiliz.enums.TipoPacote;
import agiliz.projetoAgiliz.models.Coleta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ColetaResponse(
        UUID idColeta,
        Integer romaneio,
        String statusColeta,
        VendedorColeta vendedor,
        List<ColetorResponse> coletores,
        List<PacoteResponse> pacotes,
        EnderecoColetaResponse endereco
) {

    public ColetaResponse(Coleta c) {
        this(
                c.getIdColeta(),
                c.getRomaneio(),
                c.getStatusColeta().getAlias(),
                new VendedorColeta(c.getVendedor()),
                c.getColetores() != null
                        ? c.getColetores().stream().map(ColetorResponse::new).toList()
                        : new ArrayList<>(),
                c.getPacotes() != null
                        ? c.getPacotes().stream().map(PacoteResponse::new).toList()
                        : new ArrayList<>(),
                new EnderecoColetaResponse(c.getVendedor() != null ? c.getVendedor() : null)
        );
    }

    public Integer getConferencia() {
        if (pacotes.isEmpty()) return 0;

        return pacotes.stream()
                .filter(
                        pacote -> pacote.tipo().equals(TipoPacote.COLETA.getAlias()) &&
                                pacote.status().equals(StatusPacote.CONCLUIDO.getAlias())
                )
                .toList().size();
    }

    public Integer getPacotesRestantes() {
        return this.romaneio - getConferencia();
    }
}

package agiliz.projetoAgiliz.dto.coletor;

import agiliz.projetoAgiliz.models.Coletor;

import java.time.LocalDate;
import java.util.UUID;

public record ColetorResponse(
    UUID idColetor,
    String nomeColaborador,
    String telefoneColaborador,
    LocalDate dataColeta,
    Integer pacotesColetados
) {

    public ColetorResponse(Coletor c) {
        this(
            c.getIdColetor(),
            c.getColaborador().getNomeColaborador(),
            c.getColaborador().getTelefoneColaborador(),
            c.getDataColeta(),
            c.getPacotesColetados()
        );
    }
}

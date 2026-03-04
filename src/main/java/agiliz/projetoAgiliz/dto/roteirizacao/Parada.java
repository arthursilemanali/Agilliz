package agiliz.projetoAgiliz.dto.roteirizacao;

import agiliz.projetoAgiliz.models.Pacote;

import java.util.UUID;

public record Parada(UUID idPacote, Coordenada coordenada) {

    public Parada(Pacote p) {
        this(p.getIdPacote(), p.getCoordenadas());
    }
}

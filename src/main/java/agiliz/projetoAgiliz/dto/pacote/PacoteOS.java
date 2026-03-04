package agiliz.projetoAgiliz.dto.pacote;

import agiliz.projetoAgiliz.dto.zona.ZonaRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record PacoteOS(
        @NotNull UUID idPacote
) {
}

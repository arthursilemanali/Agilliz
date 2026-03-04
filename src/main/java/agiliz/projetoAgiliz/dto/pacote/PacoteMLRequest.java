package agiliz.projetoAgiliz.dto.pacote;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record PacoteMLRequest(
        Destination destination,
        Long id
) {
}

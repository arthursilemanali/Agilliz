package agiliz.projetoAgiliz.dto.manifesto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ManifestoRequest(
    @NotNull
    @Positive
    Integer tipo,
    UUID idColeta
) {}

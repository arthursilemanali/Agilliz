package agiliz.projetoAgiliz.dto.registroManifesto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistroManifestoRequest(
    @NotNull UUID fkManifesto,
    @NotNull UUID fkPacote
) {}

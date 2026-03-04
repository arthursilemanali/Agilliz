package agiliz.projetoAgiliz.dto.registroManifesto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistroManifestoPatchRequest(
    @NotNull UUID fkManifesto
) {}

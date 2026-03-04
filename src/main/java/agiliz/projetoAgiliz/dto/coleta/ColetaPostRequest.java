package agiliz.projetoAgiliz.dto.coleta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ColetaPostRequest(
    @NotNull @Positive Integer romaneio,
    @NotNull UUID vendedor
) {}

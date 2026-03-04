package agiliz.projetoAgiliz.dto.coleta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ColetaPutRequest(
    @NotNull @Positive Integer romaneio,
    @NotNull UUID fkVendedor,
    @NotNull @Positive Integer statusColeta
) {}

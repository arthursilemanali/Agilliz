package agiliz.projetoAgiliz.dto.carteira;

import jakarta.validation.constraints.NotNull;

public record CarteiraPutRequest(
    @NotNull Integer numeroConta,
    @NotNull String chavePix,
    @NotNull String agencia
) {}

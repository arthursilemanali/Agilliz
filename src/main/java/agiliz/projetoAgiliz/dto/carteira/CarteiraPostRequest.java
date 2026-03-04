package agiliz.projetoAgiliz.dto.carteira;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record CarteiraPostRequest(
    @NotNull Integer numeroConta,
    @NotNull String chavePix,
    @NotNull String agencia,
    @NotNull UUID fkColaborador
) {}

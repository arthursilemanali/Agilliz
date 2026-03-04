package agiliz.projetoAgiliz.dto.pacote;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

// Fazer um para atualização
public record PacoteRequest(
    @NotNull @Positive Integer tipo,
    @NotNull @Positive Integer status,
    @NotNull @Positive Integer origem,
    @NotNull UUID fkDestinatario,
    @NotNull UUID fkUnidade,
    UUID fkFuncionario
) {}

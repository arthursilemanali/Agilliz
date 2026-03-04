package agiliz.projetoAgiliz.dto.transacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record TransacaoRequest(
        @NotNull @Positive Double valor,
        @NotNull UUID colaborador
) {}

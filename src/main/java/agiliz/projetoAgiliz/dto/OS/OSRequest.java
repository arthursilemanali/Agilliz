package agiliz.projetoAgiliz.dto.OS;

import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record OSRequest(
        @NotNull @PositiveOrZero Double valor,
        @NotEmpty List<EmissaoPagamentoRequest> emissoes
) {
}

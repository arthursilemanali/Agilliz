package agiliz.projetoAgiliz.dto.carteira;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AtualizacaoSaldo(
   @NotNull @Positive Double valor
) {}
